<template>
    <div class="container py-5">
        <h2>Signup</h2>
        <form @submit.prevent="handleSignup">
            <div class="mb-3">
                <label for="firstName" class="form-label">First Name</label>
                <input type="text" class="form-control" id="firstName" v-model="firstName" required>
            </div>
            <div class="mb-3">
                <label for="lastName" class="form-label">Last Name</label>
                <input type="text" class="form-control" id="lastName" v-model="lastName" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email address</label>
                <input type="email" class="form-control" id="email" v-model="email" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" v-model="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Signup</button>
            <p v-if="error" class="text-danger">{{ error }}</p>
        </form>
    </div>
</template>

<script>
import { useAuthStore } from '../stores/auth';

export default {
    name: 'Signup',
    data() {
        return {
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            error: null,
        };
    },
    methods: {
        async handleSignup() {
            const authStore = useAuthStore();
            try {
                await authStore.signup(this.firstName, this.lastName, this.email, this.password);
                this.$router.push('/login');
            } catch (error) {
                this.error = 'O cadastro falhou. Por favor, revise as informações e tente novamente.';
            }
        },
    },
};
</script>

<style scoped>
</style>
