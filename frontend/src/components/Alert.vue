<template>
    <v-container class="alert-container">
        <v-alert v-for="(alert, index) in alerts" :key="alert.id" :type="alert.type" colored-border elevation="2"
            dismissible @input="removeAlert(index)" class="mb-2">
            {{ alert.message }}
        </v-alert>
    </v-container>
</template>

<script>
export default {
    data() {
        return {
            alerts: [],
        };
    },
    methods: {
        addAlert(type, message) {
            const id = Date.now();
            this.alerts.push({ id, type, message });
            setTimeout(() => {
                this.removeAlertById(id);
            }, 3500);
        },
        removeAlert(index) {
            this.alerts.splice(index, 1);
        },
        removeAlertById(id) {
            const index = this.alerts.findIndex(alert => alert.id === id);
            if (index !== -1) {
                this.removeAlert(index);
            }
        }
    },
};
</script>

<style scoped>
.alert-container {
    position: fixed;
    top: 70px;
    left: 10px;
    z-index: 2000;
    width: 25%;
}
</style>
