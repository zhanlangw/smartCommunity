package cn.bytecloud.smartCommunity.process.dto;

import cn.bytecloud.smartCommunity.process.entity.Process;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdProcessDto extends AddProcessDto {

    @NotEmpty
    @Length(max = 40)
    private String id;


    @Override
    public Process toData(){
        Process process = super.toData();
        process.setId(id);
        return process;
    }
}
