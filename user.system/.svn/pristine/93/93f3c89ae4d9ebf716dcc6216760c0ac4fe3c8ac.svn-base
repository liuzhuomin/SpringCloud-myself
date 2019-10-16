package xinrui.cloud.config;

import com.google.common.base.Optional;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static springfox.documentation.builders.BuilderDefaults.nullToEmptyList;

/**
 * 根据position排序swagger
 */
@Component
@Primary
public class SwaggerSortCompoment extends ServiceModelToSwagger2MapperImpl {

    @Override
    public Swagger mapDocumentation(Documentation from) {
        Swagger swagger = super.mapDocumentation(from);
        swagger.setPaths( mapApiListings( from.getApiListings() ) );
//        Collection<ApiListing> values = from.getApiListings().values();
//        for (ApiListing api : values) {
//            List<ApiDescription> apis = api.getApis();
//            for (ApiDescription a : apis) {
//                List<springfox.documentation.service.Operation> operations = a.getOperations();
//                Collections.sort(operations, new Comparator<springfox.documentation.service.Operation>() {
//                    @Override
//                    public int compare(springfox.documentation.service.Operation o1, springfox.documentation.service.Operation o2) {
//                        return Integer.compare(o1.getPosition(), o2.getPosition());
//                    }
//                });
//            }
//            Collections.sort(apis, new Comparator<ApiDescription>() {
//                @Override
//                public int compare(ApiDescription left, ApiDescription right) {
//                    int leftPos = left.getOperations().size() == 1 ? left.getOperations().get(0).getPosition() : 0;
//                    int rightPos = right.getOperations().size() == 1 ? right.getOperations().get(0).getPosition() : 0;
//
//                    int position = Integer.compare(leftPos, rightPos);
//                    if (position == 0) {
//                        position = left.getPath().compareTo(right.getPath());
//                    }
//                    return position;
//                }
//            });
//        }
        return swagger;
    }

    @Override
    protected Map<String, Path> mapApiListings(Multimap<String, ApiListing> apiListings) {
        Map<String, Path> paths = new LinkedHashMap<>();
        Multimap<String, ApiListing> apiListingMap = LinkedListMultimap.create();
        Iterator iter = apiListings.entries().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, ApiListing> entry = (Map.Entry<String, ApiListing>) iter.next();
            ApiListing apis = entry.getValue();
            List<ApiDescription> apiDesc = apis.getApis();
            List<ApiDescription> newApi = new ArrayList<>();
            for (ApiDescription a : apiDesc) {
                List<springfox.documentation.service.Operation> operations = a.getOperations();
                Collections.sort(operations, new Comparator<springfox.documentation.service.Operation>() {
                    @Override
                    public int compare(springfox.documentation.service.Operation o1, springfox.documentation.service.Operation o2) {
                        return Integer.compare(o1.getPosition(), o2.getPosition());
                    }
                });
                newApi.add(a);
            }
            Collections.sort(newApi, new Comparator<ApiDescription>() {
                @Override
                public int compare(ApiDescription left, ApiDescription right) {
                    int leftPos = left.getOperations().size() == 1 ? left.getOperations().get(0).getPosition() : 0;
                    int rightPos = right.getOperations().size() == 1 ? right.getOperations().get(0).getPosition() : 0;

                    int position = Integer.compare(leftPos, rightPos);
                    if (position == 0) {
                        position = left.getPath().compareTo(right.getPath());
                    }
                    return position;
                }
            });
            try {
                //因ApiListing的属性都是final故需要通过反射来修改值
                ModifyFinalUtils.modify(apis, "apis", newApi);
            } catch (Exception e) {
                e.printStackTrace();
            }
            apiListingMap.put(entry.getKey(), apis);
        }

        for (ApiListing each : apiListingMap.values()) {
            for (ApiDescription api : each.getApis()) {
                paths.put(api.getPath(), mapOperations(api, Optional.fromNullable(paths.get(api.getPath()))));
            }
        }
        return paths;
    }

    private Path mapOperations(ApiDescription api, Optional<Path> existingPath) {
        Path path = existingPath.or(new Path());
        for (springfox.documentation.service.Operation each : nullToEmptyList(api.getOperations())) {
            Operation operation = mapOperation(each);
            path.set(each.getMethod().toString().toLowerCase(), operation);
        }
        return path;
    }
}

class ModifyFinalUtils {
    public static void modify(Object object, String fieldName, Object newFieldValue) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true); //Field 的 modifiers 是私有的
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        field.set(object, newFieldValue);
    }
}