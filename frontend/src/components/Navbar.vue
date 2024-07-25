<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-white py-3 fixed-top">
        <div class="container px-5">
            <router-link class="navbar-brand" to="/"><span class="fw-bolder text-primary">PManager</span></router-link>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0 small fw-bolder">
                    <li class="nav-item" v-if="!isAuthenticated"><router-link class="nav-link"
                            to="/login">Login</router-link></li>
                    <li class="nav-item" v-if="!isAuthenticated"><router-link class="nav-link"
                            to="/signup">Cadastrar-se</router-link></li>
                    <li class="nav-item" v-if="isAuthenticated">
                        <button class="btn btn-link nav-link" @click="handleLogout">Sair</button>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script>
export default {
    name: 'Navbar',
    data() {
        return {
            isAuthenticated: !!localStorage.getItem('accessToken'),
        };
    },
    methods: {
        handleLogout() {
            localStorage.removeItem('accessToken');
            this.$router.push('/login');
        },
    },
    watch: {
        '$route'() {
            this.isAuthenticated = !!localStorage.getItem('accessToken');
        },
    },
};
</script>

<style scoped>
/* Adicione estilos personalizados aqui, se necessário */
</style>
