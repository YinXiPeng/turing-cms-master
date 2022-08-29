package com.bonc.turing.cms.exercise.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.turing.cms.exercise.dao.mapper.HrCompanyMapper;
import com.bonc.turing.cms.exercise.dao.mapper.HrUserInfoMapper;
import com.bonc.turing.cms.exercise.dao.repository.HrCompanyRepository;
import com.bonc.turing.cms.exercise.dao.repository.HrDictionaryRepository;
import com.bonc.turing.cms.exercise.dao.repository.HrTagsRepository;
import com.bonc.turing.cms.exercise.dao.repository.HrUserInfoRepository;
import com.bonc.turing.cms.exercise.domain.HrCompany;
import com.bonc.turing.cms.exercise.domain.HrDictionary;
import com.bonc.turing.cms.exercise.domain.HrTags;
import com.bonc.turing.cms.exercise.domain.HrUserInfo;
import com.bonc.turing.cms.exercise.dto.HrCompanyDTO;
import com.bonc.turing.cms.exercise.dto.HrUserCompanyDTO;
import com.bonc.turing.cms.exercise.dto.HrUserInfoListDTO;
import com.bonc.turing.cms.exercise.service.HrUserInfoService;
import com.bonc.turing.cms.exercise.utils.GeneralException;
import com.bonc.turing.cms.exercise.vo.HrUserCompanyVO;
import com.bonc.turing.cms.exercise.vo.SearchHrUserInfoVO;
import com.bonc.turing.cms.exercise.vo.UpdateUserInfoStatus;
import com.bonc.turing.cms.manage.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/2 10:15
 */
@Service
@Slf4j
public class HrUserInfoServiceImpl implements HrUserInfoService {

    @Autowired
    private HrUserInfoMapper hrUserInfoMapper;
    @Autowired
    private HrCompanyMapper hrCompanyMapper;

    @Autowired
    private HrDictionaryRepository hrDictionaryRepository;

    @Autowired
    private HrCompanyRepository hrCompanyRepository;

    @Autowired
    private HrTagsRepository hrTagsRepository;

    @Autowired
    private HrUserInfoRepository hrUserInfoRepository;


    @Override
    public Page<HrUserInfoListDTO> list(Pageable pageable, SearchHrUserInfoVO searchHrUserInfoVO) {
        searchHrUserInfoVO.setPage(pageable.getOffset());
        searchHrUserInfoVO.setSize(pageable.getPageSize());
        Long aLong = hrUserInfoMapper.pageCount(searchHrUserInfoVO);
        List<HrUserInfoListDTO> hrUserInfoLists = hrUserInfoMapper.pageList(searchHrUserInfoVO);
        List<HrDictionary> hrDictionaries = hrDictionaryRepository.findCompanySizeAndType();
        if (CollectionUtils.isNotEmpty(hrUserInfoLists)) {
            Map<Integer, String> companySize = null;
            Map<Integer, String> companyType = null;
            if (CollectionUtils.isNotEmpty(hrDictionaries)) {
                companySize = new HashMap<>(hrDictionaries.size());
                companyType = new HashMap<>(hrDictionaries.size());
                for (HrDictionary hrDictionary : hrDictionaries) {
                    if ("公司规模".equals(hrDictionary.getDictionaryName())) {
                        companySize.put(hrDictionary.getDictionaryType(), hrDictionary.getDictionaryRemark());
                    } else {
                        companyType.put(hrDictionary.getDictionaryType(), hrDictionary.getDictionaryRemark());
                    }
                }
            }
            for (HrUserInfoListDTO hrUserInfoListDTO : hrUserInfoLists) {
                //补充是否上传营业执照
                hrUserInfoListDTO.setIsUploadLicense(StringUtils.isNotBlank(hrUserInfoListDTO.getCompanyLicense()));
                if (Objects.nonNull(companyType)) {
                    hrUserInfoListDTO.setCompanyType(companyType.get(hrUserInfoListDTO.getType()));
                }
                if (Objects.nonNull(companySize)) {
                    hrUserInfoListDTO.setCompanySize(companySize.get(hrUserInfoListDTO.getSize()));
                }
                Long useDays = (System.currentTimeMillis() - hrUserInfoListDTO.getRegisterTime()) / (1000 * 3600 * 24);
                hrUserInfoListDTO.setUseDays(useDays);
                hrUserInfoListDTO.setCompanyTags(JSON.parseArray((String) hrUserInfoListDTO.getCompanyTags()));
            }
        }
        return new PageImpl<>(hrUserInfoLists, pageable, aLong);
    }

    @Override
    public boolean judgeName(JSONObject jsonObject) {
        String companyName = jsonObject.getString("companyName");
        String companyId = jsonObject.getString("companyId");
        if (StringUtils.isBlank(companyId) || StringUtils.isBlank(companyName)) {
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误");
        }
        List<HrCompany> byCompanyName = hrCompanyRepository.findByCompanyNameNotSelf(companyName, companyId);
        return CollectionUtils.isEmpty(byCompanyName);
    }

    @Override
    public List<HrTags> getTags(JSONObject jsonObject) {
        String tagName = jsonObject.getString("tagName");
        return hrTagsRepository.findByTagTypeAndAndTagNameLike("companyTags", "%" + tagName + "%");
    }

    @Override
    public JSONObject getCompanyList() {
        PageRequest of = PageRequest.of(0, 10);
        List<HashMap> allCompany = hrCompanyMapper.findAllCompany();
        List<HrDictionary> companySize = hrDictionaryRepository.findByDictionaryName("公司规模");
        List<HrDictionary> companyType = hrDictionaryRepository.findByDictionaryName("公司类型");
        List<HrTags> tags = hrTagsRepository.findByTagTypeOrderByUsedNumDesc("companyTags", of);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyList", allCompany);
        jsonObject.put("companySize", companySize);
        jsonObject.put("companyType", companyType);
        jsonObject.put("tagList", tags);
        return jsonObject;
    }

    @Override
    public HrUserCompanyDTO getBindCompany(String guid) {
        HrUserCompanyDTO hrUserCompanyDTO = new HrUserCompanyDTO();
        HrCompany hrCompany = new HrCompany();
        HrUserInfo hrUserInfoDb = hrUserInfoRepository.findById(guid).orElse(null);
        if (Objects.isNull(hrUserInfoDb)) {
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误");
        }
        String companyId = hrUserInfoDb.getCompanyId();
        if (StringUtils.isNotBlank(companyId)) {
            HrCompany hrCompanyDb = hrCompanyRepository.findById(hrUserInfoDb.getCompanyId()).orElse(null);
            if (Objects.nonNull(hrCompanyDb)) {
                hrCompany = hrCompanyDb;
            }
        }
        BeanUtils.copyProperties(hrCompany, hrUserCompanyDTO);
        hrUserCompanyDTO.setContactName(hrUserInfoDb.getContactName());
        hrUserCompanyDTO.setContactEmail(hrUserInfoDb.getEmail());
        hrUserCompanyDTO.setContactPhone(hrUserInfoDb.getPhone());
        hrUserCompanyDTO.setCompanyLicense(hrUserInfoDb.getCompanyLicense());
        hrUserCompanyDTO.setStatus(hrUserInfoDb.getStatus());
        hrUserCompanyDTO.setGuid(guid);
        return hrUserCompanyDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(UpdateUserInfoStatus updateUserInfoStatus) {
        hrUserInfoRepository.updateStatusByGuid(updateUserInfoStatus.getStatus(), updateUserInfoStatus.getGuid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindCompany(String guid, HrUserCompanyVO hrUserCompanyVO) {
        HrCompany hrCompany = new HrCompany();
        BeanUtils.copyProperties(hrUserCompanyVO, hrCompany);
        List<HrCompany> companyNameList = hrCompanyRepository.findByCompanyNameNotSelf(hrCompany.getCompanyName(), hrCompany.getCompanyId());
        if (CollectionUtils.isNotEmpty(companyNameList)) {
            throw new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "公司名称已存在");
        }
        HrCompany hrCompanyDb = hrCompanyRepository.findById(hrCompany.getCompanyId()).orElseThrow(() -> new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误"));
        hrCompanyDb.setLogo(hrCompany.getLogo());
        hrCompanyDb.setCompanyName(hrCompany.getCompanyName());
        hrCompanyDb.setCompanyType(hrCompany.getCompanyType());
        hrCompanyDb.setCompanySize(hrCompany.getCompanySize());
        hrCompanyDb.setCompanyTags(hrCompany.getCompanyTags());
        hrCompanyDb.setCompanyIntroduction(hrCompany.getCompanyIntroduction());
        String companyTags = hrCompany.getCompanyTags();
        String substring = companyTags.substring(1, companyTags.length() - 1);
        String s1 = substring.replaceAll("\"", "");
        String[] split = s1.split(",");
        for (String s : split) {
            List<HrTags> byTagName = hrTagsRepository.findByTagNameAndTagType(s, "companyTags");
            if (null == byTagName || 0 == byTagName.size()) {
                HrTags hrTags = new HrTags();
                hrTags.setTagName(s);
                hrTags.setTagType("companyTags");
                hrTags.setUsedNum(1);
                hrTagsRepository.save(hrTags);
            } else {
                HrTags hrTags = byTagName.get(0);
                hrTags.setUsedNum(hrTags.getUsedNum() + 1);
                hrTagsRepository.save(hrTags);
            }
        }
        hrCompanyRepository.save(hrCompany);
        HrUserInfo hrUserInfo = hrUserInfoRepository.findById(guid).orElseThrow(() -> new GeneralException(ResultEntity.ResultStatus.ERROR_PARAMETER, "参数错误"));
        hrUserInfo.setCompanyId(hrCompanyDb.getCompanyId());
        if (StringUtils.isNotBlank(hrUserCompanyVO.getCompanyLicense())) {
            hrUserInfo.setCompanyLicense(hrUserCompanyVO.getCompanyLicense());
        }
        hrUserInfo.setContactName(hrUserCompanyVO.getContactName());
        hrUserInfo.setPhone(hrUserCompanyVO.getContactPhone());
        hrUserInfo.setEmail(hrUserCompanyVO.getContactEmail());
        hrUserInfo.setModifyTime(System.currentTimeMillis());
        hrUserInfoRepository.save(hrUserInfo);
    }

    @Override
    public List<HrCompanyDTO> getCompanyListLikeName(String companyName) {
        // 构造自定义查询条件
        Specification<HrCompany> queryCondition = (Specification<HrCompany>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(companyName)) {
                predicateList.add(criteriaBuilder.like(root.get("companyName"), "%" + companyName + "%"));
            }
            criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()])));
            return criteriaQuery.getRestriction();
        };

        List<HrCompany> all = hrCompanyRepository.findAll(queryCondition);
        if (CollectionUtils.isNotEmpty(all)) {
            return all.stream().map(hrCompany -> HrCompanyDTO.builder().companyId(hrCompany.getCompanyId()).companyName(hrCompany.getCompanyName()).build()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
