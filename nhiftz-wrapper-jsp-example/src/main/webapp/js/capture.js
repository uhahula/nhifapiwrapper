(function () {
  const AGENT_BASE = "https://localhost:8010/midfingerauth";
  const QUALITY = 60;
  const TIMEOUT_SECONDS = 15;
  // Wire values after Mantra's (UI index - 1) adjustment:
  //   0 = FMR_V2005, 1 = FMR_V2011, 2 = ANSI_V378.
  const TEMPLATE_FORMAT_ANSI_V378 = 2;

  const statusEl  = document.getElementById("scanner-status");
  const button    = document.getElementById("capture-btn");
  const form      = document.getElementById("auth-form");
  const imageData = document.getElementById("imageData");

  let selectedDevice = null;
  let initialized = false;

  function setBanner(cls, text) {
    statusEl.className = "status-banner " + cls;
    statusEl.textContent = text;
  }

  function isOk(data) {
    return data && String(data.ErrorCode) === "0";
  }

  function describe(data, fallback) {
    if (data && data.ErrorDescription) return data.ErrorDescription;
    return fallback;
  }

  async function post(method, body) {
    const opts = {
      method: "POST",
      headers: { "Content-Type": "application/json; charset=utf-8" }
    };
    if (body !== undefined) opts.body = JSON.stringify(body);
    const r = await fetch(AGENT_BASE + "/" + method, opts);
    if (!r.ok) throw new Error("HTTP " + r.status + " from " + method);
    return r.json();
  }

  async function getFirstConnectedDevice() {
    const data = await post("connecteddevicelist");
    if (!isOk(data)) throw new Error(describe(data, "No device found"));
    // Mantra packs the device list into ErrorDescription as
    //   "Connected devices: MFS500,...,MFS100"
    const desc = data.ErrorDescription || "";
    const afterColon = desc.split(":").slice(1).join(":");
    const devices = afterColon.split(",").map(s => s.trim()).filter(Boolean);
    if (devices.length === 0) throw new Error("No fingerprint device connected");
    return devices[0];
  }

  async function initDevice(dvc) {
    const data = await post("initdevice", { ConnectedDvc: dvc });
    if (!isOk(data)) throw new Error(describe(data, "Init failed"));
  }

  async function getInfo(dvc) {
    const data = await post("info", { ConnectedDvc: dvc });
    if (!isOk(data)) return null;
    return data.DeviceInfo || null;
  }

  async function captureFinger() {
    const data = await post("capture", { Quality: QUALITY, TimeOut: TIMEOUT_SECONDS });
    if (!isOk(data)) throw new Error(describe(data, "Capture failed"));
    return data;
  }

  async function getTemplate(tmpFormat) {
    const data = await post("gettemplate", { TmpFormat: tmpFormat });
    if (!isOk(data)) throw new Error(describe(data, "GetTemplate failed"));
    return data.ImgData;
  }

  async function bootstrap() {
    try {
      selectedDevice = await getFirstConnectedDevice();
      await initDevice(selectedDevice);
      initialized = true;
      const info = await getInfo(selectedDevice);
      const label = info
        ? info.Make + " " + info.Model + " (s/n " + info.SerialNo + ")"
        : selectedDevice;
      setBanner("ready", "Scanner ready - " + label);
      button.disabled = false;
    } catch (e) {
      setBanner("error", "Scanner not available: " + (e.message || e));
      button.disabled = true;
    }
  }

  async function captureAndSubmit() {
    button.disabled = true;
    setBanner("loading", "Place finger on scanner...");
    try {
      if (!initialized) {
        await initDevice(selectedDevice);
        initialized = true;
      }
      const cap = await captureFinger();
      const template = await getTemplate(TEMPLATE_FORMAT_ANSI_V378);
      imageData.value = template;
      setBanner("ready", "Captured (quality " + cap.Quality + ", nfiq " + cap.Nfiq + "). Submitting...");
      form.submit();
    } catch (e) {
      setBanner("error", "Capture error: " + (e.message || e));
      button.disabled = false;
    }
  }

  window.addEventListener("pagehide", function () {
    if (!initialized) return;
    try {
      fetch(AGENT_BASE + "/uninitdevice", {
        method: "POST",
        headers: { "Content-Type": "application/json; charset=utf-8" },
        keepalive: true
      }).catch(function () {});
    } catch (_) {}
  });

  button.addEventListener("click", captureAndSubmit);
  bootstrap();
})();
