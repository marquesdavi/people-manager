import axios from "axios";
import router from "../router";
import { useAuthStore } from "../stores/auth";

const api = axios.create({
	baseURL: "http://localhost:8080", // Base URL da API
});

api.interceptors.request.use(
	(config) => {
		const authStore = useAuthStore();
		if (authStore.token) {
			config.headers.Authorization = `Bearer ${authStore.token}`;
		}
		return config;
	},
	(error) => Promise.reject(error)
);

api.interceptors.response.use(
	(response) => response,
	async (error) => {
		console.error("Erro de resposta da API:", error);
		if (error.response && error.response.status === 400) {
			const authStore = useAuthStore();
			authStore.clearToken();
			router.push("/login");
		}
		return Promise.reject(error);
	}
);

export async function validateToken() {
	const authStore = useAuthStore();
	const token = authStore.token;
	if (!token) return false;

	try {
		const response = await api.post(
			"/validate-token",
			{},
			{
				headers: {
					Authorization: `Bearer ${token}`,
				},
			}
		);
		return response.status === 200;
	} catch (error) {
		console.error("Token validation error:", error);
		return false;
	}
}

export default api;
