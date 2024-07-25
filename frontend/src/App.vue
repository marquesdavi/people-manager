<template>
  <div id="app" class="d-flex flex-column h-100 w-100">
    <Navbar />
    <main class="flex-shrink-0 h-100 w-100">
      <router-view />
    </main>
    <Footer />
  </div>
</template>

<script>
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';
import Alert from './components/Alert.vue';

export default {
  name: 'App',
  components: {
    Navbar,
    Footer,
    Alert
  },
};
</script>

<style>
@import './style.css';
</style>
