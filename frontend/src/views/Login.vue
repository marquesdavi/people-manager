<template>
    <div class="container py-5">
        <h2>Login</h2>
        <form @submit.prevent="handleLogin">
            <div class="mb-3">
                <label for="email" class="form-label">Email address</label>
                <input type="email" class="form-control" id="email" v-model="email" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" v-model="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Login</button>
        </form>
        <p v-if="error" class="text-danger">{{ error }}</p>
    </div>
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

<style scoped>
/* Adicione estilos personalizados aqui, se necessário */
</style>
