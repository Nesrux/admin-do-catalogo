package com.nesrux.admin.catalogo.application.video.retrive.list;

import com.nesrux.admin.catalogo.application.UseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
