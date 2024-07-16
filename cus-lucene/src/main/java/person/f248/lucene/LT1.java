package person.f248.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LT1 {

    private static class Book {
        private Integer id;
        private String name;
        private BigDecimal price;
        private String desc;

        public void setId(Integer id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static void main(String[] args) throws IOException {
        // 1. 采集数据
        List<Book> bookList = new ArrayList<>();
        Book book1=new Book();
        book1.setId(1);
        book1.setName("Lucene");
        book1.setPrice(new BigDecimal("100.45"));
        book1.setDesc("Lucene Core is a Java library providing powerful indexing\n" +
                "and search features, as well as spellchecking, hit highlighting and advanced\n" +
                "analysis/tokenization capabilities. The PyLucene sub project provides Python\n" +
                "bindings for Lucene Core");
        bookList.add(book1);

        Book book2=new Book();
        book2.setId(2);
        book2.setName("Solr");
        book2.setPrice(new BigDecimal("66.45"));
        book2.setDesc("Solr is highly scalable, providing fully fault tolerant\n" +
                "distributed indexing, search and analytics. It exposes Lucene's features through\n" +
                "easy to use JSON/HTTP interfaces or native clients for Java and other languages");
        bookList.add(book2);

        Book book3=new Book();
        book3.setId(3);
        book3.setName("Hadoop");
        book3.setPrice(new BigDecimal("318.33"));
        book3.setDesc("The Apache Hadoop software library is a framework that\n" +
                "allows for the distributed processing of large data sets across clusters of\n" +
                "computers using simple programming models");
        bookList.add(book3);

        //2. 创建docment文档对象
        List<Document> documents = new ArrayList<>();
        bookList.forEach(x->{
            Document document=new Document();
            document.add(new TextField("id",x.getId().toString(), Field.Store.YES));
            document.add(new TextField("name",x.getName(), Field.Store.YES));
            document.add(new TextField("price",x.getPrice().toString(), Field.Store.YES));
            document.add(new TextField("desc",x.getDesc(), Field.Store.YES));
            documents.add(document);
        });
        //3.创建Analyzer分词器，对文档分词
        Analyzer analyzer=new StandardAnalyzer();
        //创建Directory对象，声明索引库的位置
        Directory directory= FSDirectory.open(Paths.get("data/output/my-lucene-indices"));
        //创建IndexWriteConfig对象，写入索引需要的配置
        IndexWriterConfig config=new IndexWriterConfig(analyzer);
        //4.创建IndexWriter对象，添加文档document
        IndexWriter indexWriter=new IndexWriter(directory,config);
        documents.forEach(doc-> {
            try {
                indexWriter.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //释放资源
        indexWriter.close();
    }
}
