package com.tyu.convert;

import com.tyu.entity.BaoMaVO;
import com.tyu.entity.CarDTO;
import com.tyu.entity.CarDetailDTO;
import com.tyu.entity.CarDetailVO;
import com.tyu.entity.CarVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-04T14:45:48+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (Oracle Corporation)"
)
@Component
public class CarConvertImpl implements CarConvert {

    @Override
    public CarVO toCarVO(CarDTO carDTO) {
        if ( carDTO == null ) {
            return null;
        }

        CarVO carVO = new CarVO();

        carVO.setTotalPrice( carDTO.getTotalPrice() );
        try {
            if ( carDTO.getCreateDate() != null ) {
                carVO.setCreateDate( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).parse( carDTO.getCreateDate() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        carVO.setCarName( carDTO.getName() );
        carVO.setCarDetailVO( carDetailDTOToCarDetailVo( carDTO.getCarDetailDTO() ) );
        carVO.setId( carDTO.getId() );

        carDTOToVOAfter( carDTO, carVO );

        return carVO;
    }

    @Override
    public CarDetailVO carDetailDTOToCarDetailVo(CarDetailDTO carDetailDTO) {
        if ( carDetailDTO == null ) {
            return null;
        }

        CarDetailVO carDetailVO = new CarDetailVO();

        carDetailVO.setCarDetailId( carDetailDTO.getId() );
        carDetailVO.setCarDetailCode( carDetailDTO.getCode() );

        return carDetailVO;
    }

    @Override
    public List<CarVO> toCarVOList(List<CarDTO> carDTOList) {
        if ( carDTOList == null ) {
            return null;
        }

        List<CarVO> list = new ArrayList<CarVO>( carDTOList.size() );
        for ( CarDTO carDTO : carDTOList ) {
            list.add( toCarVO( carDTO ) );
        }

        return list;
    }

    @Override
    public BaoMaVO toBaoMaVO2(CarDTO carDTO) {
        if ( carDTO == null ) {
            return null;
        }

        BaoMaVO baoMaVO = new BaoMaVO();

        baoMaVO.setId( carDTO.getId() );
        baoMaVO.setBrandName( carDTO.getName() );

        return baoMaVO;
    }

    @Override
    public void updateBaoMaVo(CarDTO carDTO, BaoMaVO baoMaVO) {
        if ( carDTO == null ) {
            return;
        }

        baoMaVO.setId( carDTO.getId() );
        baoMaVO.setBrandName( carDTO.getName() );
    }
}
