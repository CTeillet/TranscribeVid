<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center p-4">
    <div class="w-full max-w-2xl bg-white rounded-lg shadow-lg p-8 md:p-10">
      <HeaderComponent/>
      <TranscriptionForm/>
    </div>
  </div>
</template>

<script setup>
import HeaderComponent from './components/HeaderComponent.vue';
import TranscriptionForm from './components/TranscriptionForm.vue';
</script>
