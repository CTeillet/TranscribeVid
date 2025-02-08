<template>
  <form @submit.prevent="submitForm" class="form">
    <InputFieldComponent
      label="Video Link"
      v-model="formData.videoLink"
      type="url"
      placeholder="Enter video link"
    />
    <InputFieldComponent
      label="Email Address"
      v-model="formData.email"
      type="email"
      placeholder="Enter your email"
    />
    <SubmitButtonComponent :loading="loading" />
  </form>
</template>

<script setup>
import { reactive, ref } from 'vue';
import InputFieldComponent from './InputFieldComponent.vue';
import SubmitButtonComponent from './SubmitButtonComponent.vue';
import { useTranscriptionApi } from '@/composables/useTranscriptionApi';

const { sendTranscriptionRequest } = useTranscriptionApi();
const loading = ref(false);
const formData = reactive({
  videoLink: '',
  email: ''
});

const submitForm = async () => {
  loading.value = true;
  try {
    await sendTranscriptionRequest(formData);
    alert('Requête envoyée avec succès !');
  } catch (error) {
    alert('Une erreur est survenue lors de l\'envoi de la requête.');
  } finally {
    loading.value = false;
  }
};
</script>