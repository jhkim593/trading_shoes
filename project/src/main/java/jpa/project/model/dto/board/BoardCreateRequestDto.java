package jpa.project.model.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BoardCreateRequestDto {





        @ApiModelProperty(value = "제목", required = true)
        private String title;

        @ApiModelProperty(value = "내용", required = true)
        private String content;

}
