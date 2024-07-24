<template>
    <footer class="bg-white py-4 mt-auto">
        <div class="container px-5">
            <div class="row align-items-center justify-content-between flex-column flex-sm-row">
                <div class="col-auto">
                    <div class="small m-0">Copyright &copy; Daví Marques - 2024</div>
                </div>
                <div class="col-auto">
                    <span v-if="!isLoggedIn">
                        <a class="small"><router-link to="/login">Login</router-link></a>
                        <span class="mx-1">&middot;</span>
                        <a class="small"><router-link to="/signup">Register</router-link></a>
                    </span>
                </div>
            </div>
        </div>
    </footer>
</template>

<script>
export default {
    name: 'Footer',
    computed: {
        isLoggedIn() {
            return !!localStorage.getItem('accessToken');
        }
    }
};
</script>

<style scoped>
/* Adicione estilos personalizados aqui, se necessário */
</style>
