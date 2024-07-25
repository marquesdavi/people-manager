<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card style="width: 80%;">
            <v-card-title class="d-flex justify-space-between align-center">
                Listagem de Pessoas
                <v-btn color="primary" @click="openDialog">Cadastrar</v-btn>
            </v-card-title>
            <v-data-table :headers="headers" :items="items" :loading="loading" class="elevation-1" hide-default-footer>
                <template v-slot:[`item.actions`]="{ item }">
                    <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
                    <v-icon small class="mr-2" @click="showDetails(item.id)">mdi-eye</v-icon>
                    <v-icon small @click="confirmDelete(item.id)">mdi-delete</v-icon>
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
                    <span class="text-h5">{{ isEditMode ? 'Editar Pessoa' : 'Cadastrar Pessoa' }}</span>
                </v-card-title>
                <v-card-text>
                    <v-form @submit.prevent="handleSubmit">
                        <v-text-field v-model="person.name" label="Nome" required></v-text-field>
                        <v-text-field v-model="person.email" label="Email" type="email" required></v-text-field>
                        <v-text-field v-model="person.cpf" label="CPF" required></v-text-field>
                        <v-text-field v-model="person.birthDate" label="Data de Nascimento" type="date"
                            required></v-text-field>
                        <v-btn type="submit" color="primary">{{ isEditMode ? 'Atualizar' : 'Salvar' }}</v-btn>
                    </v-form>
                </v-card-text>
            </v-card>
        </v-dialog>

        <v-dialog v-model="confirmDeleteDialog" max-width="500px">
            <v-card>
                <v-card-title>
                    <span class="text-h5">Confirmar Exclusão</span>
                </v-card-title>
                <v-card-text>Você tem certeza de que deseja deletar esta pessoa?</v-card-text>
                <v-card-actions>
                    <v-btn color="primary" @click="performDelete">Sim</v-btn>
                    <v-btn @click="confirmDeleteDialog = false">Cancelar</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog v-model="detailsDialog" max-width="500px">
            <v-card>
                <v-card-title>
                    <span class="text-h5">Detalhes da Pessoa</span>
                </v-card-title>
                <v-card-text>
                    <p><strong>ID:</strong> {{ personDetails.id }}</p>
                    <p><strong>Nome:</strong> {{ personDetails.name }}</p>
                    <p><strong>Email:</strong> {{ personDetails.email }}</p>
                    <p><strong>CPF:</strong> {{ personDetails.cpf }}</p>
                    <p><strong>Data de Nascimento:</strong> {{ personDetails.birthDate }}</p>
                </v-card-text>
                <v-card-actions>
                    <v-btn @click="detailsDialog = false">Fechar</v-btn>
                </v-card-actions>
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
                { title: 'ID', value: 'id', sortable: true, width: '100px' },
                { title: 'Nome', value: 'name', sortable: true, align: 'left' },
                { title: 'Ações', value: 'actions', sortable: false, align: 'right', width: '150px', class: 'actions-column' },
            ],
            items: [],
            loading: true,
            options: {
                page: 1,
                sortBy: ['id'],
                sortDesc: [false],
            },
            dialog: false,
            confirmDeleteDialog: false,
            detailsDialog: false,
            person: {
                id: null,
                name: '',
                email: '',
                cpf: '',
                birthDate: '',
            },
            personDetails: {
                id: null,
                name: '',
                email: '',
                cpf: '',
                birthDate: '',
            },
            deletePersonId: null,
            isEditMode: false,
            error: null,
            isFirstPage: true,
            isLastPage: false,
        };
    },
    methods: {
        async fetchData() {
            this.loading = true;

            const itemsPerPage = 5;
            const { page, sortBy, sortDesc } = this.options;
            const currentPage = page - 1;
            const orderBy = (sortBy && sortBy.length > 0) ? sortBy[0] : 'id';
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

                this.items = response.data;
                this.isFirstPage = currentPage === 0;
                this.isLastPage = response.data.length < itemsPerPage;

                if (!this.isLastPage) {
                    const nextPageResponse = await api.get('/person/', {
                        params: {
                            page: currentPage + 1,
                            size: itemsPerPage,
                            orderBy,
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
                if (this.isEditMode) {
                    await api.patch(`/person/${this.person.id}`, this.person);
                    showAlert('success', 'Pessoa atualizada com sucesso!');
                } else {
                    await api.post('/person/', this.person);
                    showAlert('success', 'Pessoa cadastrada com sucesso!');
                }
                this.fetchData();
                this.dialog = false;
                this.resetForm();
            } catch (error) {
                showAlert('error', 'Erro ao salvar pessoa.');
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
                id: null,
                name: '',
                email: '',
                cpf: '',
                birthDate: '',
            };
            this.isEditMode = false;
        },
        openDialog() {
            this.dialog = true;
        },
        editItem(item) {
            this.person = { ...item };
            this.isEditMode = true;
            this.dialog = true;
        },
        confirmDelete(id) {
            this.deletePersonId = id;
            this.confirmDeleteDialog = true;
        },
        async performDelete() {
            try {
                await api.delete(`/person/${this.deletePersonId}`);
                showAlert('success', 'Pessoa deletada com sucesso!');
                this.fetchData();
            } catch (error) {
                showAlert('error', 'Erro ao deletar pessoa.');
            } finally {
                this.confirmDeleteDialog = false;
                this.deletePersonId = null;
            }
        },
        async showDetails(id) {
            try {
                const response = await api.get(`/person/${id}`);
                this.personDetails = response.data;
                this.detailsDialog = true;
            } catch (error) {
                showAlert('error', 'Erro ao buscar detalhes da pessoa.');
            }
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
.v-data-table .actions-column {
    position: sticky;
    right: 0;
    background-color: white;
}
</style>
