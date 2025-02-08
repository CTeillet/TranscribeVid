<template>
  <div class="mb-6">
    <label :for="label" class="block text-sm font-medium text-gray-700 mb-2">
      {{ label }}
    </label>
    <input
        :type="type"
        :id="label"
        :placeholder="placeholder"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
        class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-primary/20 focus:border-primary transition duration-200 outline-none"
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
