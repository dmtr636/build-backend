package com.kydas.build.dictionaries.documents;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.NORMATIVE_DOCUMENTS)
@Tag(name = "Сервис перечня нарушений")
public class NormativeDocumentController extends BaseController<NormativeDocument, NormativeDocumentDTO> {
}
