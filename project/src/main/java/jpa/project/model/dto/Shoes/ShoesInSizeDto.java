package jpa.project.model.dto.Shoes;

//import jpa.project.dto.RegistedShoes.RegistedShoesSimpleDto;
import jpa.project.entity.ShoesInSize;
import jpa.project.entity.ShoesStatus;
import jpa.project.entity.TradeStatus;
import jpa.project.model.dto.registedShoes.RegistedShoesSimpleDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoesInSizeDto {
    private Long id;

    private String US;

    private int stockQuantity;

    private RegistedShoesSimpleDto sellRegitedShoes;

    private RegistedShoesSimpleDto buyRegistedShoes;



    public ShoesInSizeDto(ShoesInSize shoesInSize){
        this.id=shoesInSize.getId();
        this.US=shoesInSize.getSize().getUS();
        this.stockQuantity=shoesInSize.getStockQuantity();
        int lowestPrice=0; int highestPrice=0;
        for(int i=0;i<shoesInSize.getRegistedShoes().size();i++) {
            if (shoesInSize.getRegistedShoes().get(i).getShoesStatus().equals(ShoesStatus.BID)&&shoesInSize.getRegistedShoes().get(i).getTradeStatus().equals(TradeStatus.SELL)) {
                int temp1 = shoesInSize.getRegistedShoes().get(i).getPrice();
                if (lowestPrice == 0 || lowestPrice > temp1) {
                    lowestPrice = temp1;
                   this.sellRegitedShoes=RegistedShoesSimpleDto.createRegistedSimpleDto(shoesInSize.getRegistedShoes().get(i));
                }
            }
            if (shoesInSize.getRegistedShoes().get(i).getShoesStatus().equals(ShoesStatus.BID)&&shoesInSize.getRegistedShoes().get(i).getTradeStatus().equals(TradeStatus.BUY)) {
                int temp2=shoesInSize.getRegistedShoes().get(i).getPrice();
                if(highestPrice==0||highestPrice<temp2){
                    highestPrice=temp2;
                    this.buyRegistedShoes=RegistedShoesSimpleDto.createRegistedSimpleDto(shoesInSize.getRegistedShoes().get(i));
                }
            }
        }


//        List<RegistedShoesSimpleDto>registedShoesSimpleDtoList=new ArrayList<>();
//        shoesInSize.getRegistedShoes().stream().forEach(s->{if(s.getShoesStatus().equals(ShoesStatus.BID)) registedShoesSimpleDtoList.add(new RegistedShoesSimpleDto(s));});
//        this.registedShoes=registedShoesSimpleDtoList;


    }
}
