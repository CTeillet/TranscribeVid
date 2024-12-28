<template>
  <div class="form-group">
    <label :for="label">{{ label }}</label>
    <input
        :type="type"
        :id="label"
        :placeholder="placeholder"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
    />
  </div>
</template>

<script>
export default {
  name: 'InputFieldComponent',
  props: {
    label: String,
    type: String,
    placeholder: String,
    modelValue: {
      type: String,
      required: true
    }
  },
};
</script>

<style scoped>
.form-group {
  display: flex;
  flex-direction: column;
  text-align: left;
  margin-bottom: 25px;
}

label {
  margin-bottom: 10px;
  font-size: 18px;
  color: #555;
}

input {
  padding: 15px;
  font-size: 18px;
  border: 2px solid #ddd;
  border-radius: 8px;
  outline: none;
  transition: border-color 0.3s ease;
}

input:focus {
  border-color: #4a90e2;
}
</style>
