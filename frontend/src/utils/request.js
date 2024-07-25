import axios from "axios";
import router from "../router";
import { useAuthStore } from "../stores/auth";
import { showAlert } from "../utils/alertUtil";

const api = axios.create({
	baseURL: "http://localhost:8080",
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
		if (error.response && error.response.status === 400) {
			if (
				error.response.data.message &&
				error.response.data.message.startsWith("INVCPF")
			) {
				
			} else {
				showAlert("error", "Erro ao processar a solicitação.");
			}
		} else if (error.response && error.response.status === 401) {
			const authStore = useAuthStore();
			authStore.clearToken();
			router.push("/login");
		} else {
			showAlert("error", "Ocorreu um erro inesperado.");
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
		showAlert("warning", "Sua sessão expirou. Faça login novamente!");
		return false;
	}
}

export default api;
