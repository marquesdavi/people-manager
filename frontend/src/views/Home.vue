<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 80%;">
            <v-card-title class="d-flex justify-space-between align-center">
                Listagem de Pessoas
                <v-btn @click="openDialog">Adicionar Pessoa</v-btn>
            </v-card-title>
            <v-data-table :headers="headers" :items="items" :server-items-length="totalItems" :loading="loading"
                :options.sync="options" @update:options="updateOptions" class="elevation-1" hide-default-footer>
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
                    <p v-if="error" class="text-danger">{{ error }}</p>
                </v-card-text>
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
                { text: 'Nome', value: 'name', sortable: true },
                { text: 'Email', value: 'email', sortable: true },
                { text: 'CPF', value: 'cpf', sortable: true },
                { text: 'Data de Nascimento', value: 'birthDate', sortable: true },
                { text: 'Ações', value: 'actions', sortable: false },
            ],
            items: [],
            totalItems: 0,
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

            var itemsPerPage = 6;

            const { page, sortBy, sortDesc } = this.options;
            const currentPage = page - 1; // Ajusta a página para começar de 0
            const orderBy = (sortBy && sortBy.length > 0) ? sortBy[0] : 'birthDate';
            const direction = (sortDesc && sortDesc.length > 0 && sortDesc[0]) ? 'DESC' : 'ASC';

            try {
                const response = await api.get('/person/', {
                    params: {
                        page: currentPage,
                        size: itemsPerPage,
                        orderBy,
                        direction,
                    },
                });

                this.items = response.data; // Ajuste para pegar diretamente os dados retornados
                this.totalItems = response.data.length; // Ajuste para definir o total de itens retornados

                // Atualiza os estados dos botões de navegação
                this.isFirstPage = currentPage === 0;
                this.isLastPage = response.data.length < itemsPerPage;
            } catch (error) {
                console.error('Error fetching data:', error);
            } finally {
                this.loading = false;
            }
        },
        updateOptions(options) {
            this.options = { ...this.options, ...options };
            this.fetchData();
        },
        async handleSubmit() {
            this.error = null;

            // Validações manuais
            if (!this.person.name) {
                this.error = 'Nome é obrigatório.';
                return;
            }
            if (!this.person.email || !this.validateEmail(this.person.email)) {
                this.error = 'Email inválido.';
                return;
            }
            if (!this.person.cpf || !this.validateCPF(this.person.cpf)) {
                this.error = 'CPF inválido.';
                return;
            }
            if (!this.person.birthDate) {
                this.error = 'Data de nascimento é obrigatória.';
                return;
            }

            try {
                await api.post('/person/', this.person);
                this.fetchData(); // Atualiza os dados da tabela após o cadastro
                this.dialog = false;
                this.resetForm();
            } catch (error) {
                this.error = 'Erro ao cadastrar pessoa.';
                console.error('Error creating person:', error);
            }
        },
        validateEmail(email) {
            const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@(([^<>()[\]\.,;:\s@"]+\.)+[^<>()[\]\.,;:\s@"]{2,})$/i;
            return re.test(String(email).toLowerCase());
        },
        validateCPF(cpf) {
            // Adicione a lógica de validação de CPF aqui
            // Esta é uma validação básica, você pode substituí-la por uma mais robusta
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
                    this.isLastPage = true; // Desativa o botão de próxima página
                } else {
                    this.isLastPage = false; // Ativa o botão de próxima página
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
