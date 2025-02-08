import {ref} from 'vue';

export function useTranscriptionApi() {
    const loading = ref(false);

    const loadEnvVariables = async () => {
        try {
            const response = await fetch("/env.js");
            const text = await response.text();
            eval(text);
        } catch (error) {
            console.error("Impossible de charger les variables d'environnement", error);
            throw error;
        }
    };

    const sendTranscriptionRequest = async (payload) => {
        try {
            await loadEnvVariables();
            const apiUrl = import.meta.env.VITE_API_URL;
            const VITE_API_SECRET = window._env_.VITE_API_SECRET;

            const response = await fetch(`${ apiUrl }/transcribe-requests`, {
                method: 'POST',
                headers: {
                    "Authorization": `Bearer ${ VITE_API_SECRET }`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${ response.status }`);
            }

            return await response.json();
        } catch (error) {
            console.error('Erreur lors de l\'envoi de la requête:', error);
        }
    };

    return {
        loading,
        sendTranscriptionRequest
    };
}
