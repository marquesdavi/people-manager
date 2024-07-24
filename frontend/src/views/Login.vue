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
    </div>
</template>

<script>
import axios from 'axios';

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
            try {
                const response = await axios.post('http://localhost:8080/login', {
                    email: this.email,
                    password: this.password,
                });
                const { accessToken, expiresIn } = response.data;

                // Armazene o token e o tempo de expiração conforme necessário
                localStorage.setItem('accessToken', accessToken);
                localStorage.setItem('expiresIn', expiresIn);

                // Redirecione ou faça outras ações conforme necessário
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
