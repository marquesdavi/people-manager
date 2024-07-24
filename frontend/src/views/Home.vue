<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 80%;">
            <v-card-title>
                Listagem de Pessoas
            </v-card-title>
            <v-data-table :headers="headers" :items="items" :server-items-length="totalItems" :loading="loading"
                :options.sync="options" @update:options="fetchData" class="elevation-1">
                <template v-slot:[`item.actions`]="{ item }">
                    <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
                    <v-icon small @click="deleteItem(item)">mdi-delete</v-icon>
                </template>
            </v-data-table>
        </v-card>
    </v-container>
</template>

<script>
import axios from 'axios';

export default {
    name: 'Home',
    data() {
        return {
            headers: [
                { text: 'ID', value: 'id', sortable: true },
                { text: 'Name', value: 'name', sortable: true },
                { text: 'Email', value: 'email', sortable: true },
                { text: 'CPF', value: 'cpf', sortable: true },
                { text: 'Birth Date', value: 'birthDate', sortable: true },
                { text: 'Actions', value: 'actions', sortable: false },
            ],
            items: [],
            totalItems: 0,
            loading: true,
            options: {
                page: 1,
                itemsPerPage: 5,
                sortBy: [],
                sortDesc: [],
            },
        };
    },
    methods: {
        async fetchData() {
            this.loading = true;

            const { page, itemsPerPage, sortBy, sortDesc } = this.options;

            const startRow = (page - 1) * itemsPerPage;
            const endRow = page * itemsPerPage;
            const orderBy = (sortBy && sortBy.length > 0) ? sortBy[0] : 'birthDate';
            const direction = (sortDesc && sortDesc.length > 0 && sortDesc[0]) ? 'DESC' : 'ASC';

            try {
                const token = localStorage.getItem('accessToken');
                const response = await axios.get('http://localhost:8080/person/', {
                    params: {
                        startRow,
                        endRow,
                        orderBy,
                        direction,
                    },
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                this.items = response.data.rows;
                this.totalItems = response.data.lastRow;
            } catch (error) {
                console.error('Error fetching data:', error);
            } finally {
                this.loading = false;
            }
        },
        editItem(item) {
            // Lógica para editar o item
            console.log('Edit item:', item);
        },
        deleteItem(item) {
            // Lógica para deletar o item
            console.log('Delete item:', item);
        },
    },
    watch: {
        options: {
            handler() {
                this.fetchData();
            },
            deep: true,
        },
    },
    mounted() {
        this.fetchData();
    },
};
</script>

<style scoped>
/* Adicione estilos personalizados aqui, se necessário */
</style>
