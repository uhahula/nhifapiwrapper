(function () {
  const AGENT_BASE = "http://127.0.0.1:8765";
  const statusEl   = document.getElementById("scanner-status");
  const button     = document.getElementById("capture-btn");
  const form       = document.getElementById("auth-form");
  const imageData  = document.getElementById("imageData");

  function setBanner(cls, text) {
    statusEl.className = "status-banner " + cls;
    statusEl.textContent = text;
  }

  async function probeAgent() {
    try {
      const ctrl = new AbortController();
      const t = setTimeout(() => ctrl.abort(), 1500);
      const r = await fetch(AGENT_BASE + "/status", { signal: ctrl.signal });
      clearTimeout(t);
      const body = await r.json();
      if (body.ready) {
        setBanner("ready", "Scanner ready - " + body.deviceModel + " (s/n " + body.serial + ")");
        button.disabled = false;
      } else {
        setBanner("error", "Scanner not ready: " + (body.reason || "unknown"));
        button.disabled = true;
      }
    } catch (e) {
      setBanner("error", "Cannot reach local scanner service (agent not running?)");
      button.disabled = true;
    }
  }

  async function captureAndSubmit() {
    button.disabled = true;
    setBanner("loading", "Place finger on scanner...");
    try {
      const r = await fetch(AGENT_BASE + "/capture", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ timeoutMs: 15000, minQuality: 60, templateFormat: "ANSI_V378" })
      });
      const body = await r.json();
      if (body.error) {
        setBanner("error", "Capture failed: " + body.error + (body.message ? " - " + body.message : ""));
        button.disabled = false;
        return;
      }
      imageData.value = body.template;
      setBanner("ready", "Captured (quality " + body.quality + ", nfiq " + body.nfiq + "). Submitting...");
      form.submit();
    } catch (e) {
      setBanner("error", "Capture error: " + e.message);
      button.disabled = false;
    }
  }

  button.addEventListener("click", captureAndSubmit);
  probeAgent();
})();
