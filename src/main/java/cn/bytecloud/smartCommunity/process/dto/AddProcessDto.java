package cn.bytecloud.smartCommunity.process.dto;

import cn.bytecloud.smartCommunity.process.entity.Process;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AddProcessDto {
    @NotEmpty
    @Length(max = 20)
    private String name;

    @Length(max = 100)
    private String desc;

    /**
     * dto 转 entity
     * @return
     */
    public Process toData() {
        Process process = new Process();
        process.setName(name);
        process.setDesc(desc);
        return process;
    }
}
