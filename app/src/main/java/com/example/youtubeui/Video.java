package com.example.youtubeui;

import java.io.Serializable;

/**
 * Vídeo de exemplo. Os textos ficam em strings.xml e aqui guardamos só os ids
 * dos recursos, assim a tela continua correta se o idioma mudar.
 */
public class Video implements Serializable {
    public final int titleRes;
    public final int channelRes;
    public final int viewsRes;
    public final int ageRes;
    public final int categoryRes;
    public final int thumbRes;
    public final int avatarColor;
    public final String duration;

    public Video(int titleRes, int channelRes, int viewsRes, int ageRes,
                 String duration, int categoryRes, int thumbRes, int avatarColor) {
        this.titleRes = titleRes;
        this.channelRes = channelRes;
        this.viewsRes = viewsRes;
        this.ageRes = ageRes;
        this.duration = duration;
        this.categoryRes = categoryRes;
        this.thumbRes = thumbRes;
        this.avatarColor = avatarColor;
    }
}
