<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 400px;">
            <v-card-title class="d-flex justify-center">
                <h2>Cadastrar</h2>
            </v-card-title>
            <v-card-text>
                <v-form @submit.prevent="handleSignup">
                    <v-text-field v-model="firstName" label="Nome" required>
                    </v-text-field>
                    <v-text-field v-model="lastName" label="Sobrenome" required>
                    </v-text-field>
                    <v-text-field v-model="email" label="Endereço de email" type="email" required>
                    </v-text-field>
                    <v-text-field v-model="password" label="Senha" type="password" required>
                    </v-text-field>
                    <v-btn type="submit" color="primary" block>Cadastrar</v-btn>
                </v-form>
            </v-card-text>
        </v-card>
    </v-container>
</template>

<script>
import { showAlert } from '../utils/alertUtil';
import { useAuthStore } from '../stores/auth';

export default {
    name: 'Signup',
    data() {
        return {
            firstName: '',
            lastName: '',
            email: '',
            password: '',
        };
    },
    methods: {
        async handleSignup() {

            if (!this.firstName) {
                showAlert('warning', 'Nome é obrigatório.');
                return;
            }
            if (!this.lastName) {
                showAlert('warning', 'Sobrenome é obrigatório.');
                return;
            }
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
                await authStore.signup(this.firstName, this.lastName, this.email, this.password);
                this.$router.push('/login');
            } catch (error) {
                showAlert('error', 'O cadastro falhou. Por favor, revise as informações e tente novamente.');
            }
        },
    },
};
</script>

<style scoped></style>
