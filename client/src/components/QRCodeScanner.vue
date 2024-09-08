<template>
  <div>
    <h1>QR Code Scanner</h1>
    <video ref="qrVideo" style="width: 500px;"></video>
  </div>
</template>

<script>
import QrScanner from 'qr-scanner';

export default {
  name: "QrCodeScanner",
  mounted() {
    const videoElem = this.$refs.qrVideo;

    // QR-Scanner initialisieren
    this.qrScanner = new QrScanner(videoElem, result => {
      console.log('QR-Code Inhalt:', result);
      this.qrScanner.stop(); // Stoppt den Scanner nach einem erfolgreichen Scan
    });

    // Webcam aktivieren und Scanner starten
    this.qrScanner.start();
  },
  beforeDestroy() {
    // Scanner stoppen, wenn die Komponente zerstört wird
    if (this.qrScanner) {
      this.qrScanner.stop();
    }
  }
};
</script>

<style scoped>
/* Optionales Styling */
video {
  max-width: 100%;
  border: 2px solid #ccc;
}
</style>
