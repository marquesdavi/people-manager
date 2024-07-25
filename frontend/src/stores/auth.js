import { defineStore } from "pinia";
import api from "../utils/request";

export const useAuthStore = defineStore("auth", {
	state: () => ({
		token: localStorage.getItem("accessToken") || null,
	}),
	actions: {
		setToken(token) {
			this.token = token;
			localStorage.setItem("accessToken", token);
		},
		clearToken() {
			this.token = null;
			localStorage.removeItem("accessToken");
		},
		async login(email, password) {
			try {
				const response = await api.post("/login", { email, password });
				const { accessToken, expiresIn } = response.data;
				this.setToken(accessToken);
				localStorage.setItem("expiresIn", expiresIn);
			} catch (error) {
				showAlert("error", "Erro ao fazer login.");
			}
		},
		async signup(firstName, lastName, email, password) {
			try {
				await api.post("/user/", {
					firstName,
					lastName,
					email,
					password,
				});
			} catch (error) {
				showAlert("error", "Erro ao tentar registrar.");
			}
		},
	},
});
