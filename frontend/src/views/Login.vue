<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 400px;">
            <v-card-title class="d-flex justify-center">
                <h2>Login</h2>
            </v-card-title>
            <v-card-text>
                <v-form @submit.prevent="handleLogin">
                    <v-text-field v-model="email" label="Email address" type="email" required>
                    </v-text-field>
                    <v-text-field v-model="password" label="Password" type="password" required>
                    </v-text-field>
                    <v-btn type="submit" color="primary" block>Login</v-btn>
                </v-form>
                <v-alert v-if="error" type="error" class="mt-3">{{ error }}</v-alert>
            </v-card-text>
        </v-card>
    </v-container>
</template>

<script>
import { useAuthStore } from '../stores/auth';

export default {
    name: 'Login',
    data() {
        return {
            email: '',
            password: '',
            error: null,
        };
    },
    methods: {
        async handleLogin() {
            const authStore = useAuthStore();
            try {
                await authStore.login(this.email, this.password);
                this.$router.push('/');
            } catch (error) {
                this.error = 'Login failed. Please check your credentials and try again.';
            }
        },
    },
};
</script>

<style scoped></style>
