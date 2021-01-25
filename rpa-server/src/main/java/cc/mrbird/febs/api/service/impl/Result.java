package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaStockPositionList;
import cc.mrbird.febs.api.mapper.RpaStockPositionMapper;
import cc.mrbird.febs.api.service.IRpaStockPositionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import cc.mrbird.febs.common.entity.FebsConstant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 业务处理的成功和失败状态
 */
public enum Result{
    SUCCESS,
    FAILED,
}