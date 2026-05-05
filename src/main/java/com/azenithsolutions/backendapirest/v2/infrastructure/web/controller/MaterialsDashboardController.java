package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.CommodityPriceEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataCommodityPriceRepository;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v2/materials")
@Tag(name = "Materials Price Dashboard - V2", description = "KPIs e insights de preço de matérias-primas")
@RequiredArgsConstructor
public class MaterialsDashboardController {

    private final SpringDataCommodityPriceRepository commodityRepository;

    private static final Map<String, String> TRACKED_MATERIALS = new LinkedHashMap<>();
    static {
        TRACKED_MATERIALS.put("Copper",    "Cobre");
        TRACKED_MATERIALS.put("Gold",      "Ouro");
        TRACKED_MATERIALS.put("Iron Ore",  "Ferro");
    }

    private static final List<TopComponent> TOP_COMPONENTS = List.of(
        new TopComponent(72L,   "JM38510/11108BEA", "1801-0071", "High-Speed Drivers with Dual SPDT JFET Switches"),
        new TopComponent(129L,  "JM38510/10901",    "1801-0128", "Precision Timers"),
        new TopComponent(444L,  "JM38510/10102BCB", "1801-0443", "Linear IC"),
        new TopComponent(1344L, "JM38510",          "1801-1472", "JM38510 Series Component")
    );

    @GetMapping("/dashboard")
    @Operation(
        summary = "Dashboard de preços de matérias-primas",
        description = "Retorna preços atuais (BRL), histórico, previsão e recomendações de compra para as matérias-primas e componentes monitorados. Dados originados da tabela commodities_prices (ETL)."
    )
    public ResponseEntity<ApiResponseDTO<?>> getDashboard(HttpServletRequest request) {
        try {
            LocalDate today = LocalDate.now();

            List<MaterialDashboard> materials = new ArrayList<>();
            Map<String, MaterialSummary> summaryByMaterial = new HashMap<>();

            for (Map.Entry<String, String> entry : TRACKED_MATERIALS.entrySet()) {
                String metalKey   = entry.getKey();
                String displayName = entry.getValue();

                List<CommodityPriceEntity> all = commodityRepository.findByMetalsOrderByDateAsc(metalKey);
                if (all.isEmpty()) continue;

                List<CommodityPriceEntity> history = all.stream()
                    .filter(p -> !p.getDate().isAfter(today))
                    .collect(Collectors.toList());
                if (history.size() > 12) {
                    history = history.subList(history.size() - 12, history.size());
                }

                List<CommodityPriceEntity> forecast = all.stream()
                    .filter(p -> p.getDate().isAfter(today))
                    .collect(Collectors.toList());

                if (history.isEmpty()) continue;

                double currentPrice  = history.get(history.size() - 1).getPriceBrl().doubleValue();
                double previousPrice = history.size() > 1
                    ? history.get(history.size() - 2).getPriceBrl().doubleValue()
                    : currentPrice;

                double changePct = previousPrice == 0 ? 0
                    : round2((currentPrice - previousPrice) / previousPrice * 100.0);
                String trend = changePct > 0.5 ? "UP" : (changePct < -0.5 ? "DOWN" : "STABLE");

                String unit = history.get(history.size() - 1).getUnit();

                double forecast3mAvg = forecast.stream()
                    .limit(3)
                    .mapToDouble(p -> p.getPriceBrl().doubleValue())
                    .average()
                    .orElse(currentPrice);

                double forecastChangePct = currentPrice == 0 ? 0
                    : round2((forecast3mAvg - currentPrice) / currentPrice * 100.0);

                String recommendation;
                String reason;
                if (forecastChangePct >= 2.0) {
                    recommendation = "BUY_NOW";
                    reason = String.format("Previsão de alta de %.2f%% nos próximos 3 meses — antecipar compra.", forecastChangePct);
                } else if (forecastChangePct <= -2.0) {
                    recommendation = "WAIT";
                    reason = String.format("Previsão de queda de %.2f%% nos próximos 3 meses — aguardar.", Math.abs(forecastChangePct));
                } else {
                    recommendation = "STABLE";
                    reason = "Preço estável projetado — sem urgência para comprar.";
                }

                materials.add(new MaterialDashboard(
                    metalKey, displayName, unit,
                    round2(currentPrice), round2(previousPrice), changePct, trend,
                    history.stream()
                        .map(p -> new ChartPoint(p.getDate().toString(), round2(p.getPriceBrl().doubleValue())))
                        .collect(Collectors.toList()),
                    forecast.stream()
                        .map(p -> new ChartPoint(p.getDate().toString(), round2(p.getPriceBrl().doubleValue())))
                        .collect(Collectors.toList()),
                    recommendation, reason, forecastChangePct
                ));

                summaryByMaterial.put(metalKey, new MaterialSummary(currentPrice, previousPrice, forecast3mAvg));
            }

            List<ComponentInsight> components = new ArrayList<>();
            if (summaryByMaterial.size() == TRACKED_MATERIALS.size()) {
                double currentIdx  = avg(summaryByMaterial.values().stream().mapToDouble(s -> s.current).toArray());
                double previousIdx = avg(summaryByMaterial.values().stream().mapToDouble(s -> s.previous).toArray());
                double forecastIdx = avg(summaryByMaterial.values().stream().mapToDouble(s -> s.forecast3mAvg).toArray());

                double idxChangePct   = previousIdx == 0 ? 0 : round2((currentIdx - previousIdx) / previousIdx * 100.0);
                double idxForecastPct = currentIdx  == 0 ? 0 : round2((forecastIdx - currentIdx)  / currentIdx  * 100.0);

                String compRec;
                String compReason;
                if (idxForecastPct >= 2.0) {
                    compRec    = "BUY_NOW";
                    compReason = String.format("Custo de matéria-prima projetado para subir %.2f%%. Comprar agora.", idxForecastPct);
                } else if (idxForecastPct <= -2.0) {
                    compRec    = "WAIT";
                    compReason = String.format("Custo deve cair %.2f%% nos próximos 3 meses. Aguardar.", Math.abs(idxForecastPct));
                } else {
                    compRec    = "STABLE";
                    compReason = "Custo estável — momento neutro para reposição.";
                }

                for (TopComponent c : TOP_COMPONENTS) {
                    components.add(new ComponentInsight(
                        c.id, c.partNumber, c.idHardwaretech, c.description,
                        round2(currentIdx), round2(previousIdx), idxChangePct,
                        compRec, compReason
                    ));
                }
            }

            DashboardPayload payload = new DashboardPayload(today.toString(), materials, components);

            return ResponseEntity.ok(new ApiResponseDTO<>(
                LocalDateTime.now(), HttpStatus.OK.value(), "Success", payload, request.getRequestURI()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>(
                LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error", List.of(e.getMessage()), request.getRequestURI()
            ));
        }
    }

    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }

    private static double avg(double[] arr) {
        if (arr.length == 0) return 0;
        double s = 0;
        for (double v : arr) s += v;
        return s / arr.length;
    }

    private record TopComponent(Long id, String partNumber, String idHardwaretech, String description) {}
    private record MaterialSummary(double current, double previous, double forecast3mAvg) {}

    public record ChartPoint(String date, double price) {}

    public record MaterialDashboard(
        String name, String displayName, String unit,
        double currentPriceBRL, double previousPriceBRL, double changePercent, String trend,
        List<ChartPoint> history, List<ChartPoint> forecast,
        String recommendation, String recommendationReason, double forecastChangePercent
    ) {}

    public record ComponentInsight(
        Long id, String partNumber, String idHardwaretech, String description,
        double compositeIndexBRL, double previousCompositeIndexBRL, double changePercent,
        String recommendation, String reason
    ) {}

    public record DashboardPayload(
        String referenceDate,
        List<MaterialDashboard> materials,
        List<ComponentInsight> components
    ) {}
}
