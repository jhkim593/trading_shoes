package jpa.project.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BoardCreateRequestDto {



        @NotEmpty
        @Size(min=2, max=100)
        @ApiModelProperty(value = "제목", required = true)
        private String title;
        @Size(min=2, max=500)
        @ApiModelProperty(value = "내용", required = true)
        private String content;

}
