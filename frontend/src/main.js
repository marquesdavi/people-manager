import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";
import { createVuetify } from "vuetify";
import "vuetify/styles";
import "@mdi/font/css/materialdesignicons.css";
import api, { validateToken } from "./utils/request";

const vuetify = createVuetify();

const app = createApp(App);
app.use(router);
app.use(vuetify);

router.beforeEach(async (to, from, next) => {
	const isAuthenticated = !!localStorage.getItem("accessToken");

	if (to.name === "Login" || to.name === "Signup") {
		return next();
	}

	if (isAuthenticated) {
		const tokenValid = await validateToken();
		if (tokenValid) {
			return next();
		}
		localStorage.removeItem("accessToken");
		return next({ name: "Login" });
	}

	if (to.matched.some((record) => record.meta.requiresAuth)) {
		return next({ name: "Login" });
	}

	next();
});

app.mount("#app");
