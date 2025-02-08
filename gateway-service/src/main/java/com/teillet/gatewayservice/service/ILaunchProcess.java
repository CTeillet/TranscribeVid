package com.teillet.gatewayservice.service;

import com.teillet.gatewayservice.dto.TranscribeRequestDto;
import com.teillet.shared.model.LaunchProcessRequest;

public interface ILaunchProcess {
	/**
	 * Lance le processus de transcription d'une vidéo.
	 *
	 * @param requestDto L'objet DTO contenant les informations de la demande de transcription.
	 * @return L'entité {@link LaunchProcessRequest} sauvegardée après son enregistrement en base de données.
	 * <p>
	 * Cette méthode effectue les étapes suivantes :
	 * <ul>
	 *     <li>Transforme le DTO en entité via un mapper.</li>
	 *     <li>Sauvegarde l'entité dans la base de données.</li>
	 *     <li>Crée une requête de téléchargement avec un horodatage.</li>
	 *     <li>Envoie un message Kafka pour demander le téléchargement de la vidéo.</li>
	 *     <li>Retourne l'entité sauvegardée.</li>
	 * </ul>
	 * <p>
	 * Les logs permettent de suivre l'exécution du processus et un message Kafka est envoyé
	 * pour initier le téléchargement de la vidéo.
	 */
	LaunchProcessRequest launchProcess(TranscribeRequestDto requestDto);
}
