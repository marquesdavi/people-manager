import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import Signup from "../views/Signup.vue";

const routes = [
	{ path: "/", name: "Home", component: Home },
	{ path: "/login", name: "Login", component: Login },
	{ path: "/signup", name: "Signup", component: Signup },
];

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes,
});

router.beforeEach((to, from, next) => {
	const isAuthenticated = !!localStorage.getItem("accessToken");
	if (to.name !== "Login" && !isAuthenticated) next({ name: "Login" });
	else next();
});

export default router;
