import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface Structure {
    // zwraca dowolny element o podanym kolorze
    Optional<Block> findBlockByColor(String color);

    // zwraca wszystkie elementy z danego materiału
    List<Block> findBlocksByMaterial(String material);

    //zwraca liczbę wszystkich elementów tworzących strukturę
    int count();
}

public class Wall implements Structure {
    private List<Block> blocks;

    public Wall(List<Block> blocks) {
        this.blocks = blocks;
    }

    //Sprawdza czy w liście blocks znajdują się instancję CompositeBlock
    public boolean containsCompositeBlocks() {
        return blocks.stream().anyMatch(block -> block instanceof CompositeBlock);
    }

    //Zwraca bloki, z których składają się CompositeBlocks
    public List<Block> getInternalBlocks() {
        List<Block> internalBlocks = new ArrayList<>();

        for (Block block : blocks) {
            if (block instanceof CompositeBlock)
                internalBlocks.addAll(((CompositeBlock) block).getBlocks());
        }

        return internalBlocks;
    }

    //Zwraca wszystkie bloki, włącznie z tymi, z których składają się CompositeBlocks
    public List<Block> getAllBlocks() {
        if (containsCompositeBlocks()) {
            List<Block> allBlocks = new ArrayList<>(blocks);
            allBlocks.addAll(getInternalBlocks());
            return allBlocks;
        }

        return blocks;
    }

    //Założyłem, że w poniższych metodach interesują nas także bloki wewnątrz potencjalnych CompositeBlocks
    @Override
    public Optional<Block> findBlockByColor(String color) {

        return getAllBlocks().stream()
                .filter(block -> block.getColor().equals(color))
                .findFirst();
    }

    @Override
    public List<Block> findBlocksByMaterial(String material) {
        return getAllBlocks().stream()
                .filter(block -> block.getMaterial().equals(material))
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        int blocksCount = 0;

        for (Block block : blocks) {
            if (block instanceof CompositeBlock)
                //CompositeBlock liczę jako sumę bloków, z których się składa
                blocksCount += ((CompositeBlock) block).getBlocks().size();
            else
                blocksCount++;
        }

        return blocksCount;
    }
}

interface Block {
    String getColor();

    String getMaterial();
}

interface CompositeBlock extends Block {
    List<Block> getBlocks();
}

