package cn.bytecloud.smartCommunity.address.service;

import cn.bytecloud.smartCommunity.constant.ModelConstant;
import cn.bytecloud.smartCommunity.address.dao.AddressDao;
import cn.bytecloud.smartCommunity.address.dao.AddressRepository;
import cn.bytecloud.smartCommunity.address.dto.AddressDto;
import cn.bytecloud.smartCommunity.address.entity.Address;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.util.AddressUtil;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private MongoTemplate template;
    @Autowired
    private AddressDao dao;

    @Override
    public void add(MultipartFile file) throws IOException {
        repository.deleteAll();
        XSSFWorkbook sheets = new XSSFWorkbook(file.getInputStream());
        for(int i = 0;i<18;i++){
            for (Row row :sheets.getSheetAt(i)) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    String stringCellValue = cell.getStringCellValue();
                    Address address = new Address();
                    String[] split = stringCellValue.split(",");
                    address.setLongitude(split[0]);
                    address.setLatitude(split[1]);
                    System.out.println(i);
                    System.out.println(stringCellValue);
                    System.out.println(row.getRowNum());
                    address.setCreateTime(System.currentTimeMillis());
                    address.setId(UUIDUtil.getUUID());
                    address.setNum(i);
                    save(address);
                }
            }
        }

    }

    public synchronized void save(Address address){
        try {
            Thread.sleep(11);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repository.save(address);
    }

    @Override
    public Object getData() {
        List list = new ArrayList();
        for (int i = 0;i<18;i++) {
            Query query = new Query();
            query.addCriteria(Criteria.where(ModelConstant.ADDRESS_NUM).is(i));
            query.with(new Sort(Sort.Direction.DESC, ModelConstant.CREATE_TIME));
            List<AddressDto> data = EntityUtil.entityListToDtoList(template.find(query,Address.class), AddressDto.class);
            list.add(data);
        }
        return list;
    }

    @Override
    public String findUnitIdByLatitudeAndLongitude(String latitude, String longitude) throws ByteException {
        for (int i = 0;i<18;i++) {
            List<Point2D.Double> list = new ArrayList<>();
            List<Address> addresses = dao.findByNum(i);
            addresses.forEach(address -> {
                list.add(new Point2D.Double(Double.parseDouble(address.getLongitude()), Double.parseDouble(address.getLatitude())));
            });
            if (AddressUtil.IsPtInPoly(new Point2D.Double(Double.parseDouble(longitude), Double.parseDouble(latitude)), list)) {
                return addresses.get(0).getUnitId();
            }
        }
        throw new ByteException("没有找到对应街区");
    }

}
