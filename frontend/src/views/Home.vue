<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 80%;">
            <v-card-title class="d-flex justify-space-between align-center">
                Listagem de Pessoas
                <v-btn color="primary" @click="openDialog">Adicionar Pessoa</v-btn>
            </v-card-title>
            <v-data-table :headers="headers" :items="items" :loading="loading" class="elevation-1" hide-default-footer>
                <template v-slot:[`item.actions`]="{ item }">
                    <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
                    <v-icon small @click="deleteItem(item)">mdi-delete</v-icon>
                </template>
            </v-data-table>
            <v-card-actions class="d-flex justify-space-between">
                <v-btn @click="prevPage" :disabled="isFirstPage">Voltar Página</v-btn>
                <v-btn @click="nextPage" :disabled="isLastPage">Avançar Página</v-btn>
            </v-card-actions>
        </v-card>
        <v-dialog v-model="dialog" max-width="500px">
            <v-card>
                <v-card-title>
                    <span class="text-h5">Cadastrar Pessoa</span>
                </v-card-title>
                <v-card-text>
                    <v-form @submit.prevent="handleSubmit">
                        <v-text-field v-model="person.name" label="Nome" required></v-text-field>
                        <v-text-field v-model="person.email" label="Email" type="email" required></v-text-field>
                        <v-text-field v-model="person.cpf" label="CPF" required></v-text-field>
                        <v-text-field v-model="person.birthDate" label="Data de Nascimento" type="date"
                            required></v-text-field>
                        <v-btn type="submit" color="primary">Salvar</v-btn>
                    </v-form>
                </v-card-text>
            </v-card>
        </v-dialog>
    </v-container>
</template>

<script>
import api from '../utils/request';
import { showAlert } from '../utils/alertUtil';

export default {
    name: 'Home',
    data() {
        return {
            headers: [
                { text: 'ID', value: 'id', sortable: true },
                { text: 'Nome', value: 'name', sortable: true },
                { text: 'Email', value: 'email', sortable: true },
                { text: 'CPF', value: 'cpf', sortable: true },
                { text: 'Data de Nascimento', value: 'birthDate', sortable: true },
                { text: 'Ações', value: 'actions', sortable: false },
            ],
            items: [],
            loading: true,
            options: {
                page: 1,
                sortBy: [],
                sortDesc: [],
            },
            dialog: false,
            person: {
                name: '',
                email: '',
                cpf: '',
                birthDate: '',
            },
            error: null,
            isFirstPage: true,
            isLastPage: false,
        };
    },
    methods: {
        async fetchData() {
            this.loading = true;

            const itemsPerPage = 5;
            const { page, sortDesc } = this.options;
            const currentPage = page - 1;
            const direction = (sortDesc && sortDesc.length > 0 && sortDesc[0]) ? 'DESC' : 'ASC';

            try {
                const response = await api.get('/person/', {
                    params: {
                        page: currentPage,
                        size: itemsPerPage,
                        direction,
                    },
                });

                this.items = response.data;
                this.isFirstPage = currentPage === 0;
                this.isLastPage = response.data.length < itemsPerPage;

                if (!this.isLastPage) {
                    const nextPageResponse = await api.get('/person/', {
                        params: {
                            page: currentPage + 1,
                            size: itemsPerPage,
                            direction,
                        },
                    });
                    this.isLastPage = nextPageResponse.data.length === 0;
                }
            } catch (error) {
                showAlert('error', 'Erro ao buscar dados.');
            } finally {
                this.loading = false;
            }
        },
        async handleSubmit() {
            this.error = null;

            if (!this.person.name) {
                showAlert('error', 'Nome é obrigatório.');
                return;
            }
            if (!this.person.email || !this.validateEmail(this.person.email)) {
                showAlert('error', 'Email inválido.');
                return;
            }
            if (!this.person.cpf || !this.validateCPF(this.person.cpf)) {
                showAlert('error', 'CPF inválido.');
                return;
            }
            if (!this.person.birthDate) {
                showAlert('error', 'Data de nascimento é obrigatória.');
                return;
            }

            try {
                await api.post('/person/', this.person);
                this.fetchData();
                this.dialog = false;
                this.resetForm();
                showAlert('success', 'Pessoa cadastrada com sucesso!');
            } catch (error) {
                showAlert('error', 'Erro ao cadastrar pessoa.');
            }
        },
        validateEmail(email) {
            const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@(([^<>()[\]\.,;:\s@"]+\.)+[^<>()[\]\.,;:\s@"]{2,})$/i;
            return re.test(String(email).toLowerCase());
        },
        validateCPF(cpf) {
            const re = /^\d{11}$/;
            return re.test(cpf);
        },
        resetForm() {
            this.person = {
                name: '',
                email: '',
                cpf: '',
                birthDate: '',
            };
        },
        openDialog() {
            this.dialog = true;
        },
        editItem(item) {
            // Lógica para editar o item
            console.log('Edit item:', item);
        },
        deleteItem(item) {
            // Lógica para deletar o item
            console.log('Delete item:', item);
        },
        async prevPage() {
            if (this.options.page > 1) {
                this.options.page--;
                await this.fetchData();
            }
        },
        async nextPage() {
            if (!this.isLastPage) {
                this.options.page++;
                await this.fetchData();
                if (this.items.length === 0) {
                    this.options.page--;
                    this.isLastPage = true;
                } else {
                    this.isLastPage = false;
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
