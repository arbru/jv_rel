package net.brusdeilins.jv_rel.rest;

import java.util.function.Function;

import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphDto;
import net.brusdeilins.jv_rel.graph.api.dto.JvrGraphInfoDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryDto;
import net.brusdeilins.jv_rel.query.api.dto.JvrQueryInfoDto;

public class Transformer {
    public static Function<JvrGraphDto, JvrGraphInfoDto> GRAPH_TO_GRAPH_INFO_DTO = graphDto -> {
        JvrGraphInfoDto graphInfoDto = new JvrGraphInfoDto();
        graphInfoDto.setId(graphDto.getId());
        graphInfoDto.setName(graphDto.getName());
        return graphInfoDto;
    };

    public static Function<JvrQueryDto, JvrQueryInfoDto> QUERY_TO_QUERY_INFO_DTO = queryDto -> {
        JvrQueryInfoDto queryInfoDto = new JvrQueryInfoDto();
        queryInfoDto.setName(queryDto.getName());
        return queryInfoDto;
    };


}
