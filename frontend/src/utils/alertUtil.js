import { createApp } from "vue";
import Alert from "../components/Alert.vue";
import { vuetify } from "../main.js";

let alertInstance = null;

function initAlert() {
	if (!alertInstance) {
		const div = document.createElement("div");
		document.body.appendChild(div);
		const app = createApp(Alert);
		app.use(vuetify);
		alertInstance = app.mount(div);
	}
}

export function showAlert(type, message) {
	initAlert();
	alertInstance.addAlert(type, message);
}
