package com.jzo2o.market.model.dto.request;

import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.market.enums.ActivityTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("活动保存请求模型")
@Validated
public class ActivitySaveReqDTO {
    @ApiModelProperty(value = "活动id,新增时不填，修改时必填",required = false)
    private Long id;

    @ApiModelProperty(value = "活动名称",required = true)
    @Size(max = 20, message = "活动名称超出20个字符无法输入")
    @Null(message = "活动名称为空，请输入活动名称")
    private String name;

    @ApiModelProperty(value = "优惠券类型，1：满减，2：折扣",required = true)
    private Integer type;
    @ApiModelProperty(value = "满减限额，0：表示无门槛，其他值：最低消费金额")
    @Min(value = 0, message = "满额限制请输入大于/等于 0的整数")
    @Null(message = "满额限制为空，请输入满额限制")
    private BigDecimal amountCondition;

    @ApiModelProperty(value = "折扣率，折扣类型的折扣率，例如：8,打8折, type为2时必填",required = false)
    private Integer discountRate;

    @ApiModelProperty(value = "优惠金额，满减或无门槛的优惠金额",required = true)
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "发放开始时间",required = true)
    @Null(message = "发放时间为空，请输入发放时间")
    private LocalDateTime distributeStartTime;
    @Null(message = "发放时间为空，请输入发放时间")
    @ApiModelProperty(value = "发放结束时间",required = true)
    private LocalDateTime distributeEndTime;

    @ApiModelProperty(value = "发放数量，0：表示无限量，其他正数表示最大发放量",required = false)
    private Integer totalNum = 0;

    @ApiModelProperty(value = "有效期天数",required = true)
    @Null(message = "使用期限请输入大于0的整数")
    @Min(value = 0, message = "使用期限请输入大于0的整数")
    private Integer validityDays;

    public void check() {
        if(ActivityTypeEnum.AMOUNT_DISCOUNT.equals(type)) {
            // 满减
            //discountAmount字段不能为空，且值为正数
            if(ObjectUtils.isNull(discountAmount)) {
                throw new BadRequestException("折扣金额为空，请输入折扣金额");
            }else if(discountAmount.compareTo(BigDecimal.ZERO) <0){
                throw new BadRequestException("折扣金额请输入大于0的整数");
            }
        }else if(ActivityTypeEnum.RATE_DISCOUNT.equals(type)) {
            // 折扣
            if(ObjectUtils.isNull(discountRate)) {
                throw new BadRequestException("折扣比例为空，请输入折扣比例");
            }else if(discountRate.compareTo(0) < 0 || discountRate.compareTo(100) > 0) {
                throw new BadRequestException("折扣比例请输入大于0，小于10的整数");
            }
        }else {
            throw new BadRequestException("优惠券类型不存在");
        }
        // 发放时间
        if(distributeStartTime.isAfter(distributeEndTime)){
            throw new BadRequestException("发放结束时间不能早于发放开始时间");
        }
        if(distributeStartTime.isBefore(DateUtils.now())) {
            throw new BadRequestException("发放开始时间不能早于当前时间");
        }
    }

}
