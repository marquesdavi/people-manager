<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 80%;">
            <v-card-title class="d-flex justify-space-between align-center">
                Listagem de Pessoas
                <v-btn color="primary" @click="openModal">Cadastrar</v-btn>
            </v-card-title>
            <v-data-table :headers="headers" :items="items" :server-items-length="totalItems" :loading="loading"
                :options.sync="options" @update:options="fetchData" class="elevation-1">
                <template v-slot:[`item.actions`]="{ item }">
                    <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
                    <v-icon small @click="deleteItem(item)">mdi-delete</v-icon>
                </template>
            </v-data-table>
        </v-card>

        <v-dialog v-model="showModal" max-width="600px">
            <v-card>
                <v-card-title>
                    <span class="headline">Cadastrar Pessoa</span>
                </v-card-title>
                <v-card-text>
                    <v-form ref="form" v-model="valid" lazy-validation>
                        <v-text-field v-model="newPerson.name" :rules="nameRules" label="Name" required></v-text-field>
                        <v-text-field v-model="newPerson.email" :rules="emailRules" label="Email"
                            required></v-text-field>
                        <v-text-field v-model="newPerson.cpf" :rules="cpfRules" label="CPF" required></v-text-field>
                        <v-date-input v-model="newPerson.birthDate" label="Birth Date" :rules="dateRules"
                            required></v-date-input>
                    </v-form>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="closeModal">Cancelar</v-btn>
                    <v-btn color="blue darken-1" text @click="createPerson">Salvar</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>

<script>
import api from '../utils/request';

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
                itemsPerPage: 10,
                sortBy: [],
                sortDesc: [],
            },
            showModal: false,
            newPerson: {
                name: '',
                email: '',
                cpf: '',
                birthDate: '',
            },
            valid: true,
            nameRules: [
                v => !!v || 'Name is required',
                v => (v && v.length <= 50) || 'Name must be less than 50 characters',
            ],
            emailRules: [
                v => !!v || 'Email is required',
                v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
            ],
            cpfRules: [
                v => !!v || 'CPF is required',
                v => /^\d{11}$/.test(v) || 'CPF must be 11 digits',
            ],
            dateRules: [
                v => !!v || 'Birth Date is required',
                v => /^\d{4}-\d{2}-\d{2}$/.test(v) || 'Birth Date must be in the format YYYY-MM-DD',
            ],
        };
    },
    methods: {
        async fetchData() {
            this.loading = true;

            const { page, itemsPerPage, sortBy, sortDesc } = this.options;

            const startRow = (page - 1) * itemsPerPage;
            const endRow = startRow + itemsPerPage;
            const orderBy = (sortBy && sortBy.length > 0) ? sortBy[0] : 'birthDate';
            const direction = (sortDesc && sortDesc.length > 0 && sortDesc[0]) ? 'DESC' : 'ASC';

            try {
                const response = await api.get('/person/', {
                    params: {
                        startRow,
                        endRow,
                        orderBy,
                        direction,
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
        openModal() {
            this.showModal = true;
        },
        closeModal() {
            this.showModal = false;
            this.newPerson = {
                name: '',
                email: '',
                cpf: '',
                birthDate: '',
            };
        },
        async createPerson() {
            if (this.$refs.form.validate()) {
                try {
                    await api.post('/person/', {
                        name: this.newPerson.name,
                        email: this.newPerson.email,
                        cpf: this.newPerson.cpf,
                        birthDate: this.newPerson.birthDate,
                    });
                    this.closeModal();
                    this.fetchData();
                } catch (error) {
                    console.error('Error creating person:', error);
                }
            }
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
