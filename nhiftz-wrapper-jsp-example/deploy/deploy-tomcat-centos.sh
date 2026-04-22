#!/usr/bin/env bash
#
# Deploy nhiftz-wrapper-jsp-example to a Tomcat instance on CentOS/RHEL.
#
# Copies the WAR into $CATALINA_HOME/webapps, writes the NHIF_* environment
# variables into $CATALINA_BASE/bin/setenv.sh (created if missing), and
# restarts the tomcat systemd service.
#
# Usage:
#   sudo CONFIG_FILE=./nhif.prod.env ./deploy-tomcat-centos.sh path/to/war
#
# CONFIG_FILE format (one KEY=VALUE per line, no quotes):
#   NHIF_AUTH_URL=https://test.nhif.or.tz
#   NHIF_SERVICE_URL=https://test.nhif.or.tz/servicehub
#   NHIF_CLIENT_ID=11014
#   NHIF_CLIENT_SECRET=...
#   NHIF_USERNAME=Mtundi
#
# Environment overrides:
#   CATALINA_HOME  (default /usr/share/tomcat)
#   CATALINA_BASE  (default $CATALINA_HOME)
#   SERVICE_NAME   (default tomcat)
#   CONTEXT_NAME   (default nhiftz-wrapper-jsp-example)

set -euo pipefail

CATALINA_HOME="${CATALINA_HOME:-/usr/share/tomcat}"
CATALINA_BASE="${CATALINA_BASE:-$CATALINA_HOME}"
SERVICE_NAME="${SERVICE_NAME:-tomcat}"
CONTEXT_NAME="${CONTEXT_NAME:-nhiftz-wrapper-jsp-example}"
CONFIG_FILE="${CONFIG_FILE:-}"

WAR="${1:-}"
if [[ -z "$WAR" || ! -f "$WAR" ]]; then
  echo "usage: $0 <path-to.war>" >&2
  exit 2
fi
if [[ -z "$CONFIG_FILE" || ! -f "$CONFIG_FILE" ]]; then
  echo "CONFIG_FILE env var must point to an NHIF_* env file" >&2
  exit 2
fi

required=(NHIF_AUTH_URL NHIF_SERVICE_URL NHIF_CLIENT_ID NHIF_CLIENT_SECRET NHIF_USERNAME)
for k in "${required[@]}"; do
  if ! grep -qE "^${k}=" "$CONFIG_FILE"; then
    echo "CONFIG_FILE is missing $k" >&2
    exit 2
  fi
done

SETENV="$CATALINA_BASE/bin/setenv.sh"
WEBAPPS="$CATALINA_BASE/webapps"

echo "==> Stopping $SERVICE_NAME"
systemctl stop "$SERVICE_NAME"

echo "==> Writing NHIF_* exports into $SETENV"
# Rewrite the block between the markers, leaving anything else intact.
tmp="$(mktemp)"
if [[ -f "$SETENV" ]]; then
  awk '/^# >>> nhif-config/{flag=1; next} /^# <<< nhif-config/{flag=0; next} !flag{print}' "$SETENV" > "$tmp"
fi
{
  echo "# >>> nhif-config (managed by deploy-tomcat-centos.sh)"
  while IFS='=' read -r k v; do
    # skip empty lines and comments
    [[ -z "$k" || "${k:0:1}" == "#" ]] && continue
    # only export NHIF_* keys
    [[ "$k" =~ ^NHIF_ ]] || continue
    # single-quote the value, escape any embedded single quotes
    escaped=${v//\'/\'\\\'\'}
    echo "export $k='$escaped'"
  done < "$CONFIG_FILE"
  echo "# <<< nhif-config"
} >> "$tmp"
install -o tomcat -g tomcat -m 0640 "$tmp" "$SETENV"
rm -f "$tmp"

echo "==> Removing old deployment at $WEBAPPS/$CONTEXT_NAME"
rm -rf "$WEBAPPS/$CONTEXT_NAME" "$WEBAPPS/$CONTEXT_NAME.war"

echo "==> Copying $WAR -> $WEBAPPS/$CONTEXT_NAME.war"
install -o tomcat -g tomcat -m 0640 "$WAR" "$WEBAPPS/$CONTEXT_NAME.war"

echo "==> Starting $SERVICE_NAME"
systemctl start "$SERVICE_NAME"

echo "==> Waiting for /health to return 200"
deadline=$((SECONDS + 60))
url="http://127.0.0.1:8080/$CONTEXT_NAME/health"
while (( SECONDS < deadline )); do
  if code=$(curl -sk -o /dev/null -w '%{http_code}' "$url"); then
    if [[ "$code" == "200" ]]; then
      echo "    health OK"
      curl -sk "$url"; echo
      exit 0
    fi
  fi
  sleep 2
done

echo "health probe did not return 200 within 60s; recent logs:" >&2
journalctl -u "$SERVICE_NAME" -n 40 --no-pager >&2 || true
exit 1
