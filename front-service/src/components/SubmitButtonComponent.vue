<template>
  <button type="submit" class="btn-submit">Submit</button>
</template>

<script>
export default {
  name: 'SubmitButtonComponent'
};
</script>

<style scoped>
.btn-submit {
  padding: 15px;
  font-size: 18px;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-submit:hover {
  background-color: #357ABD;
}
</style>
