package com.teillet.shared.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("LaunchProcessRequest")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaunchProcessRequest implements Serializable {
	@Id
	private String requestId;
	private String email;
	private String videoUrl;
}
