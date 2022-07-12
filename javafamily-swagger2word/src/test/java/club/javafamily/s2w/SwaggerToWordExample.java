package club.javafamily.s2w;

import club.javafamily.view.*;
import club.javafamily.view.Response;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.BookmarkTextRenderData;
import com.deepoove.poi.data.HyperlinkTextRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.plugin.highlight.HighlightRenderData;
import com.deepoove.poi.plugin.highlight.HighlightRenderPolicy;
import com.deepoove.poi.plugin.highlight.HighlightStyle;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.*;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@DisplayName("Swagger to Word Example")
public class SwaggerToWordExample {

     String location = "https://hxt.yijiweather.com/hxt-api/v2/api-docs";
//    String location = "src/test/resources/swagger/petstore.json";

    @Test
    public void testSwaggerToWord() throws IOException {
        SwaggerParser swaggerParser = new SwaggerParser();
        Swagger swagger = swaggerParser.read(location);
        SwaggerView viewData = convert(swagger);

        LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder()
                .bind("parameters", hackLoopTableRenderPolicy)
                .bind("responses", hackLoopTableRenderPolicy)
                .bind("properties", hackLoopTableRenderPolicy)
                .bind("definitionCode", new HighlightRenderPolicy())
                .useSpringEL()
                .build();

        final ClassPathResource templateResource
                = new ClassPathResource("swagger.docx");

        XWPFTemplate template = XWPFTemplate.compile(templateResource.getInputStream(), config)
                .render(viewData);

//        XWPFTemplate template = XWPFTemplate.compile("E:\\Repositories\\javafamily-service\\javafamily-swagger2word\\src\\main\\resources\\swagger.docx", config)
//                .render(viewData);

        // 输出文件
        final File outputFile = new File("E:\\Repositories\\javafamily-service\\javafamily-swagger2word\\target\\接口文档.docx");

        if(!outputFile.exists()) {
            if(!outputFile.createNewFile()) {
                throw new RuntimeException("创建文件失败!");
            }
            else {
                log.info("在 {} 创建了新的文件.", outputFile.getAbsolutePath());
            }
        }

        template.writeAndClose(new FileOutputStream(outputFile));
    }

    @SuppressWarnings("rawtypes")
    private SwaggerView convert(Swagger swagger) {
        SwaggerView view = new SwaggerView();
        view.setInfo(swagger.getInfo());
        view.setBasePath(swagger.getBasePath());
        view.setExternalDocs(swagger.getExternalDocs());
        view.setHost(swagger.getHost());
        view.setSchemes(swagger.getSchemes());
        List<Resource> resources = new ArrayList<>();

        List<Endpoint> endpoints = new ArrayList<>();
        Map<String, Path> paths = swagger.getPaths();
        if (null == paths)
            return view;
        paths.forEach((url, path) -> {

            if (null == path.getOperationMap())
                return;
            path.getOperationMap().forEach((method, operation) -> {
                Endpoint endpoint = new Endpoint();
                endpoint.setUrl(url);
                endpoint.setHttpMethod(method.toString());
                endpoint.setGet(HttpMethod.GET == method);
                endpoint.setDelete(HttpMethod.DELETE == method);
                endpoint.setPut(HttpMethod.PUT == method);
                endpoint.setPost(HttpMethod.POST == method);

                endpoint.setSummary(operation.getSummary());
                endpoint.setDescription(operation.getDescription());
                endpoint.setTag(operation.getTags());
                endpoint.setProduces(operation.getProduces());
                endpoint.setConsumes(operation.getConsumes());

                if (null != operation.getParameters()) {
                    List<Parameter> parameters = new ArrayList<>();
                    operation.getParameters().forEach(para -> {
                        Parameter parameter = new Parameter();
                        parameter.setDescription(para.getDescription());
                        parameter.setIn(para.getIn());
                        parameter.setName(para.getName());
                        parameter.setRequired(para.getRequired());
                        List<TextRenderData> schema = new ArrayList<>();
                        if (para instanceof AbstractSerializableParameter) {
                            io.swagger.models.properties.Property items = ((AbstractSerializableParameter) para).getItems();
                            String type = ((AbstractSerializableParameter) para).getType();
                            // if array
                            if (ArrayProperty.isType(type)) {
                                schema.add(new TextRenderData("<"));
                                schema.addAll(formatProperty(items));
                                schema.add(new TextRenderData(">"));
                            } else {
                                schema.addAll(formatProperty(items));
                            }

                            // parameter type or array
                            if (StringUtils.isNotBlank(type)) {
                                schema.add(new TextRenderData(type));
                            }
                            if (StringUtils.isNotBlank(((AbstractSerializableParameter) para).getCollectionFormat())) {
                                schema.add(new TextRenderData(
                                        "(" + ((AbstractSerializableParameter) para).getCollectionFormat() + ")"));
                            }
                        }
                        if (para instanceof BodyParameter) {
                            Model schemaModel = ((BodyParameter) para).getSchema();
                            schema.addAll(fomartSchemaModel(schemaModel));
                        }
                        parameter.setSchema(schema);
                        parameters.add(parameter);
                    });
                    endpoint.setParameters(parameters);
                }

                if (null != operation.getResponses()) {
                    List<Response> responses = new ArrayList<>();
                    operation.getResponses().forEach((code, resp) -> {
                        Response response = new Response();
                        response.setCode(code);
                        response.setDescription(resp.getDescription());
                        Model schemaModel = resp.getResponseSchema();
                        response.setSchema(fomartSchemaModel(schemaModel));

                        if (null != resp.getHeaders()) {
                            List<Header> headers = new ArrayList<>();
                            resp.getHeaders().forEach((name, property) -> {
                                Header header = new Header();
                                header.setName(name);
                                header.setDescription(property.getDescription());
                                header.setType(property.getType());
                                headers.add(header);
                            });
                            response.setHeaders(headers);
                        }
                        responses.add(response);
                    });
                    endpoint.setResponses(responses);
                }
                endpoints.add(endpoint);
            });

        });

        swagger.getTags().forEach(tag -> {
            Resource resource = new Resource();
            resource.setName(tag.getName());
            resource.setDescription(tag.getDescription());
            resource.setEndpoints(endpoints.stream()
                    .filter(path -> (null != path.getTag() && path.getTag().contains(tag.getName())))
                    .collect(Collectors.toList()));
            resources.add(resource);
        });
        view.setResources(resources);

        ObjectMapper objectMapper = new ObjectMapper();
        if (null != swagger.getDefinitions()) {
            List<Definition> definitions = new ArrayList<>();
            swagger.getDefinitions().forEach((name, model) -> {
                Definition definition = new Definition();
                definition.setName(new BookmarkTextRenderData(name, name));
                if (null != model.getProperties()) {
                    List<Property> properties = new ArrayList<>();
                    model.getProperties().forEach((key, prop) -> {
                        Property property = new Property();
                        property.setName(key);
                        property.setDescription(prop.getDescription());
                        property.setRequired(prop.getRequired());
                        property.setSchema(formatProperty(prop));
                        properties.add(property);
                    });
                    definition.setProperties(properties);
                    Map map = valueOfModel(swagger.getDefinitions(), model, new HashSet<>());
                    try {
                        String writeValueAsString = objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(map);
                        HighlightRenderData code = new HighlightRenderData();
                        code.setCode(writeValueAsString);
                        code.setLanguage("json");
                        code.setStyle(HighlightStyle.builder().withTheme("zenburn").build());
                        definition.setDefinitionCode(code);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }

                if(definition.getDefinitionCode() != null) {
                    definitions.add(definition);
                }
            });
            view.setDefinitions(definitions);
        }
        return view;
    }

    private Map<String, Object> valueOfModel(Map<String, Model> definitions, Model model, Set<String> keyCache) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        model.getProperties().forEach((key, prop) -> {
            Object value = valueOfProperty(definitions, prop, keyCache);
            map.put(key, value);
        });
        return map;
    }

    private Object valueOfProperty(Map<String, Model> definitions, io.swagger.models.properties.Property prop, Set<String> keyCache) {
        Object value;
        if (prop instanceof RefProperty) {
            String ref = ((RefProperty) prop).get$ref().substring("#/definitions/".length());

            if(ObjectUtils.nullSafeEquals(ref, definitions.get(ref).getTitle())) {
                return "-";
            }

            if (keyCache.contains(ref))
                value = ((RefProperty) prop).get$ref();
            else
                value = valueOfModel(definitions, definitions.get(ref), keyCache);
        } else if (prop instanceof ArrayProperty) {
            List<Object> list = new ArrayList<>();
            io.swagger.models.properties.Property insideItems = ((ArrayProperty) prop).getItems();
            list.add(valueOfProperty(definitions, insideItems, keyCache));
            value = list;
        } else if (prop instanceof AbstractNumericProperty) {
            value = 0;
        } else if (prop instanceof BooleanProperty) {
            value = false;
        } else {
            value = prop.getType();
        }
        return value;
    }

    private List<TextRenderData> fomartSchemaModel(Model schemaModel) {
        List<TextRenderData> schema = new ArrayList<>();
        if (null == schemaModel)
            return schema;
        // if array
        if (schemaModel instanceof ArrayModel) {
            io.swagger.models.properties.Property items = ((ArrayModel) schemaModel).getItems();
            schema.add(new TextRenderData("<"));
            schema.addAll(formatProperty(items));
            schema.add(new TextRenderData(">"));
            schema.add(new TextRenderData(((ArrayModel) schemaModel).getType()));
        } else
            // if ref
            if (schemaModel instanceof RefModel) {
                String ref = ((RefModel) schemaModel).get$ref().substring("#/definitions/".length());
                schema.add(new HyperlinkTextRenderData(ref, "anchor:" + ref));
            } else if (schemaModel instanceof ModelImpl) {
                schema.add(new TextRenderData(((ModelImpl) schemaModel).getType()));
            }
        // ComposedModel
        return schema;
    }

    private List<TextRenderData> formatProperty(io.swagger.models.properties.Property items) {
        List<TextRenderData> schema = new ArrayList<>();
        if (null != items) {
            if (items instanceof RefProperty) {
                String ref = ((RefProperty) items).get$ref().substring("#/definitions/".length());
                schema.add(new HyperlinkTextRenderData(ref, "anchor:" + ref));
            } else if (items instanceof ArrayProperty) {
                io.swagger.models.properties.Property insideItems = ((ArrayProperty) items).getItems();
                schema.add(new TextRenderData("<"));
                schema.addAll(formatProperty(insideItems));
                schema.add(new TextRenderData(">"));
                schema.add(new TextRenderData(items.getType()));
            } else {
                schema.add(new TextRenderData(items.getType()));
            }
        }
        return schema;
    }

}
