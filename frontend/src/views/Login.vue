<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 400px;">
            <v-card-title class="d-flex justify-center">
                <h2>Entrar</h2>
            </v-card-title>
            <v-card-text>
                <v-form @submit.prevent="handleLogin">
                    <v-text-field v-model="email" label="Endereço de email" type="email" required>
                    </v-text-field>
                    <v-text-field v-model="password" label="Senha" type="password" required>
                    </v-text-field>
                    <v-btn type="submit" color="primary" block>Entrar</v-btn>
                </v-form>
            </v-card-text>
        </v-card>
    </v-container>
</template>

<script>
import { showAlert } from '../utils/alertUtil';
import { useAuthStore } from '../stores/auth';

export default {
    name: 'Login',
    data() {
        return {
            email: '',
            password: '',
        };
    },
    methods: {
        async handleLogin() {

            if (!this.email) {
                showAlert('warning', 'Email é obrigatório.');
                return;
            }
            if (!this.password) {
                showAlert('warning', 'Senha é obrigatória.');
                return;
            }

            const authStore = useAuthStore();
            try {
                await authStore.login(this.email, this.password);
                this.$router.push('/');
            } catch (error) {
                showAlert('error', 'Falha no login. Por favor, verifique suas credenciais e tente novamente.');
            }
        },
    },
};
</script>

<style scoped></style>
