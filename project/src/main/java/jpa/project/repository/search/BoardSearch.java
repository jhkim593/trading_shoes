package jpa.project.repository.search;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class BoardSearch {
    private String content;
    private String title;
}
