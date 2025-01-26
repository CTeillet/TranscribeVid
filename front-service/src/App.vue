<template>
  <div id="app" class="container">
    <HeaderComponent/>

    <form @submit.prevent="submitForm" class="form">
      <InputFieldComponent
          label="Video Link"
          v-model="videoLink"
          type="url"
          placeholder="Enter video link"
      />
      <InputFieldComponent
          label="Email Address"
          v-model="email"
          type="email"
          placeholder="Enter your email"
      />

      <SubmitButtonComponent/>
    </form>
  </div>
</template>

<script>
import HeaderComponent from './components/HeaderComponent.vue';
import InputFieldComponent from './components/InputFieldComponent.vue';
import SubmitButtonComponent from './components/SubmitButtonComponent.vue';

export default {
  components: {
    HeaderComponent,
    InputFieldComponent,
    SubmitButtonComponent
  },
  data() {
    return {
      videoLink: '',
      email: ''
    };
  },
  methods: {
    submitForm() {
      // alert(`Video Link: ${this.videoLink}, Email: ${this.email}`);
      this.sendRequest();
    },
    async sendRequest() {
      try {
        const payload = {
          videoUrl: this.videoLink,
          email: this.email
        };

        const apiUrl = import.meta.env.VITE_API_URL; // Charger l'URL depuis les variables d'environnement

        const response = await fetch(`${ apiUrl }/transcribe-requests`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(payload)
        });

        if (!response.ok) {
          console.log(`HTTP error! status: ${ response.status }`);
        }

        const result = await response.json();
        alert('Requête envoyée avec succès  !');
        console.log('Réponse:', result);
      } catch (error) {
        console.error('Erreur lors de l\'envoi de la requête:', error);
        alert('Une erreur est survenue lors de l\'envoi de la requête.');
      }
    }
  }
};
</script>

<style scoped>
/* Même style que précédemment */
.container {
  background-color: #fff;
  padding: 50px;
  border-radius: 15px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 600px;
  width: 100%;
  margin: auto;
}

html, body {
  height: 100%;
}

body {
  display: flex;
  justify-content: center;
  align-items: center;
}

@media (max-width: 600px) {
  .container {
    padding: 40px;
  }
}
</style>
