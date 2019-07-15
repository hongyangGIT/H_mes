package com.hongyang.beans;

import java.util.List;

import com.google.common.collect.Lists;
import com.hongyang.model.MesOrder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {

    private List<T> data = Lists.newArrayList();

    private int total = 0;
}
