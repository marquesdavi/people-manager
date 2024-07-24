import axios from "axios";
import router from "../router";

// Cria uma instância do axios com configuração base
const api = axios.create({
	baseURL: "http://localhost:8080", // Base URL da API
});

// Intercepta todas as requisições para adicionar o token de autenticação
api.interceptors.request.use(
	async (config) => {
		const token = localStorage.getItem("accessToken");
		if (token) {
			config.headers.Authorization = `Bearer ${token}`;
		}
		return config;
	},
	(error) => Promise.reject(error)
);

// Intercepta todas as respostas para lidar com tokens expirados
api.interceptors.response.use(
	(response) => response,
	async (error) => {
		if (error.response && error.response.status === 401) {
			localStorage.removeItem("accessToken");
			router.push("/login");
		}
		return Promise.reject(error);
	}
);

// Função para verificar a validade do token
export async function validateToken() {
	const token = localStorage.getItem("accessToken");
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
