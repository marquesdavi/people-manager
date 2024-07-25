<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 400px;">
            <v-card-title class="d-flex justify-center">
                <h2>Signup</h2>
            </v-card-title>
            <v-card-text>
                <v-form @submit.prevent="handleSignup">
                    <v-text-field v-model="firstName" label="First Name" required>
                    </v-text-field>
                    <v-text-field v-model="lastName" label="Last Name" required>
                    </v-text-field>
                    <v-text-field v-model="email" label="Email address" type="email" required>
                    </v-text-field>
                    <v-text-field v-model="password" label="Password" type="password" required>
                    </v-text-field>
                    <v-btn type="submit" color="primary" block>Signup</v-btn>
                </v-form>
                <v-alert v-if="error" type="error" class="mt-3">{{ error }}</v-alert>
            </v-card-text>
        </v-card>
    </v-container>
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

<style scoped></style>
